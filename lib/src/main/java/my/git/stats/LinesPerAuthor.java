package my.git.stats;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.PersonIdent;

public class LinesPerAuthor {

   static class DirBlameResult {
      static void mergeMaps(Map<String, Integer> source, Map<String, Integer> destination) {
         for (Entry<String, Integer> entry : source.entrySet()) {
            Integer value = destination.get(entry.getKey());
            if (value != null) {
               destination.put(entry.getKey(), entry.getValue() + value);
            } else {
               destination.put(entry.getKey(), entry.getValue());
            }
         }
      }

      static Map<String, Integer> sumResults(DirBlameResult dirResult) {
         Map<String, Integer> map = new HashMap<>();

         for (FileBlameResult file : dirResult.childFileResults) {
            mergeMaps(file.linesPerAuthor, map);
         }

         for (DirBlameResult dir : dirResult.childDirResults) {
            mergeMaps(DirBlameResult.sumResults(dir), map);
         }

         return map;
      }

      List<FileBlameResult> childFileResults;

      List<DirBlameResult> childDirResults;

      DirBlameResult(List<FileBlameResult> childFileResults, List<DirBlameResult> childDirResults) {
         this.childFileResults = childFileResults;
         this.childDirResults = childDirResults;
      }

   }

   static class FileBlameResult {
      File file;
      Map<String, Integer> linesPerAuthor;

      FileBlameResult(File file, Map<String, Integer> linesPerAuthor) {
         this.file = file;
         this.linesPerAuthor = linesPerAuthor;
      }
   }

   private static FileBlameResult countFile(Git git, File file, int[] counts) {
      try {
         Map<String, Integer> linesPerAuthorMap = new HashMap<>();
         String fileName = file.toString()
               .substring(git.getRepository().getWorkTree().toString().length() + 1)
               .replace("\\", "/");

         // Create a BlameCommand object
         BlameResult blameResult = git.blame().setFilePath(fileName.toString())
               .setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();

         // Iterate through the BlameResult object
         if (blameResult == null) {
            return null;
         }

         counts[0]++;
         for (int i = 0; blameResult != null && i < blameResult.getResultContents().size(); i++) {
            counts[1]++;
            // Get the author of the current line
            PersonIdent author = blameResult.getSourceAuthor(i);
            String authorName = author.getName();
            // Check if the author is already in the HashMap
            if (linesPerAuthorMap.containsKey(authorName)) {
               // If yes, increment the number of lines
               linesPerAuthorMap.put(authorName, linesPerAuthorMap.get(authorName) + 1);
            } else {
               // If no, add the author to the HashMap with a value of 1
               linesPerAuthorMap.put(authorName, 1);
            }
         }

         return new FileBlameResult(file, linesPerAuthorMap);
      } catch (GitAPIException e) {
         e.printStackTrace();
      }
      return null;
   }

   private static DirBlameResult countFiles(Git git, File[] files, int[] counts) {

      List<FileBlameResult> fileBlames = new ArrayList<>();
      List<DirBlameResult> dirBlames = new ArrayList<>();

      for (File file : files) {
         if (file.isDirectory() && !file.getName().equals(".git")) {
            DirBlameResult result = countFiles(git, file.listFiles(), counts);
            if (!result.childDirResults.isEmpty() || !result.childFileResults.isEmpty()) {
               dirBlames.add(result);
            }

         } else if (Files.isRegularFile(file.toPath(), new LinkOption[0])) {
            FileBlameResult result = countFile(git, file, counts);
            if (result != null) {
               fileBlames.add(result);
            }
         }
      }

      return new DirBlameResult(fileBlames, dirBlames);
   }

   public static Git getGitRepo() throws IOException {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnVal = fileChooser.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {

         // Use the GitRepoUtils.getAuthors() method to read the selected file
         return Git.open(new File(fileChooser.getSelectedFile().getAbsolutePath()));
      }

      return null;
   }

   public static void main(String[] args) throws IOException {
      // Initialize a Git repository object
      Git git = getGitRepo();

      // Set the file path for the blame command
      File rootDirectory = git.getRepository().getWorkTree();

      long timeStart = System.nanoTime();
      int[] counts = new int[2];

      File[] files = rootDirectory.listFiles();
      DirBlameResult result = countFiles(git, files, counts);

      Map<String, Integer> linesPerAuthor = DirBlameResult.sumResults(result);

      // Print the results
      for (String author : linesPerAuthor.keySet()) {
         System.out.println(author + ": " + linesPerAuthor.get(author) + " lines");
      }

      double deltaTimeSec = (System.nanoTime() - timeStart) / Math.pow(10, 9);
      System.out.println(String.format("Total run time: %.2f sec", deltaTimeSec));
      System.out.println(String.format("Total files: %d", counts[0]));
      System.out.println(String.format("Total lines: %d", counts[1]));
      System.out.println(String.format("%.2f sec per file, %.2f milisec per line",
            deltaTimeSec / counts[0], deltaTimeSec / counts[1] * 1000));
   }

   public static void walkFiles(File rootDirectory, Git git, int[] counts,
         Map<String, Integer> linesPerAuthor) throws IOException {

      Path rootPath = rootDirectory.toPath();

      // use the walk method to recursively walk through all the files and directories
      // only process regular files
      Files.walk(Paths.get(rootDirectory.toURI())).filter(Files::isRegularFile).forEach(path -> {
         try {
            String fileName = rootPath.relativize(path).toString().replace("\\", "/");

            if (!fileName.contains(".git")) {
               // Create a BlameCommand object
               BlameResult blameResult = git.blame().setFilePath(fileName.toString())
                     .setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();

               // Iterate through the BlameResult object
               if (blameResult != null) {
                  counts[0]++;
               }
               for (int i = 0; blameResult != null
                     && i < blameResult.getResultContents().size(); i++) {
                  counts[1]++;
                  // Get the author of the current line
                  PersonIdent author = blameResult.getSourceAuthor(i);
                  String authorName = author.getName();
                  // Check if the author is already in the HashMap
                  if (linesPerAuthor.containsKey(authorName)) {
                     // If yes, increment the number of lines
                     linesPerAuthor.put(authorName, linesPerAuthor.get(authorName) + 1);
                  } else {
                     // If no, add the author to the HashMap with a value of 1
                     linesPerAuthor.put(authorName, 1);
                  }
               }
            }
         } catch (GitAPIException e) {
            e.printStackTrace();
         }
      });

   }
}
