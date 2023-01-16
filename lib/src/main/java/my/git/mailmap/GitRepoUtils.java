package my.git.mailmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitRepoUtils {

   /**
    * Retrieves a list of authors from the commit history of a git repository.
    * 
    * @param git the git repository on the local filesystem
    * @return a list of Author objects, each representing an author with a name and
    *         email
    * @throws IOException
    * @throws Exception   if an error occurs while reading the repository or
    *                     accessing its commit history
    */
   public static List<Author> getAuthors(Git git) throws IOException {
      List<Author> authors = new ArrayList<>();

      Iterable<RevCommit> commits;
      try {
         commits = git.log().all().call();
      } catch (GitAPIException | IOException e) {
         return new ArrayList<>();
      }
      for (RevCommit commit : commits) {
         String name = commit.getAuthorIdent().getName();
         String email = commit.getAuthorIdent().getEmailAddress();
         authors.add(new Author(name, email));
      }

      return authors;
   }

}
