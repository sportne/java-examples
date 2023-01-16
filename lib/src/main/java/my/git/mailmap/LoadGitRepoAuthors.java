package my.git.mailmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.eclipse.jgit.api.Git;

import my.git.mailmap.gui.SortedListModel;

public class LoadGitRepoAuthors implements ActionListener {

   private JList<Author> unassignedAuthorsList;
   private Set<Author> unassignedAuthorsSet;

   private Set<Author> primaryAuthorSet;

   private JFrame parent;

   LoadGitRepoAuthors(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
         Set<Author> primaryAuthorSet) {
      this(unassignedAuthorsList, unassignedAuthorsSet, primaryAuthorSet, null);
   }

   public LoadGitRepoAuthors(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
         Set<Author> primaryAuthorSet, JFrame parent) {
      this.unassignedAuthorsList = unassignedAuthorsList;
      this.unassignedAuthorsSet = unassignedAuthorsSet;
      this.primaryAuthorSet = primaryAuthorSet;
      this.parent = parent;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnVal = fileChooser.showOpenDialog(parent);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         try {
            // Use the GitRepoUtils.getAuthors() method to read the selected file
            List<Author> authors = GitRepoUtils
                  .getAuthors(Git.open(new File(fileChooser.getSelectedFile().getAbsolutePath())));
            // get the models for the two lists of items
            SortedListModel<Author> unassignedModel = (SortedListModel<Author>) (unassignedAuthorsList
                  .getModel());

            // check for duplicate authors in the primary model and unassigned model
            for (Author author : authors) {
               if (!primaryAuthorSet.contains(author) && !unassignedAuthorsSet.contains(author)) {
                  unassignedModel.addElement(author);
                  unassignedAuthorsSet.add(author);
               }
            }
         } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error reading git repo.");
         }
      }
   }

}
