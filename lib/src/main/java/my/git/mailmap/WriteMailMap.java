package my.git.mailmap;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;

import my.git.mailmap.gui.MailMapAuthorTreeNode;
import my.git.mailmap.gui.MailMapAuthorTreeRoot;

public class WriteMailMap implements ActionListener {

   private Frame parent;
   private MailMapAuthorTreeRoot root;

   /**
    * @param parent
    * @param root
    */
   public WriteMailMap(Frame parent, MailMapAuthorTreeRoot root) {
      this.parent = parent;
      this.root = root;
   }

   /**
    * Constructor for testing.
    *
    * @param root the root element of the JTree to write from.
    */
   WriteMailMap(MailMapAuthorTreeRoot root) {
      this(null, root);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser = new JFileChooser();
      int returnVal = fileChooser.showSaveDialog(parent);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         try {
            // Update the mailMapEntries list with the current state of the
            // primaryAuthorsList
            List<MailMapEntry> mailMapEntries = collectAllEntries();

            // Use the MailMapEntry.writeMailMap() method to save the current state of the
            // JLists to the selected file
            MailMapEntry.writeMailMap(fileChooser.getSelectedFile().getAbsolutePath(),
                  mailMapEntries);
         } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error writing mailmap file.");
         }
      }
   }

   /**
    * Collects all entries from the root's children, which are of type
    * {@link MailMapAuthorTreeNode}.
    *
    * @return a list of {@link MailMapEntry} objects representing all entries in
    *         the tree
    */
   private List<MailMapEntry> collectAllEntries() {
      // Initialize a list to store all entries
      List<MailMapEntry> allEntries = new ArrayList<>();
      // Iterate through the children of the root
      for (TreeNode node : root.getChildren()) {

         // Check if the child is a MailMapAuthorTreeNode
         if (node instanceof MailMapAuthorTreeNode) {
            // If so, add all entries from the MailMapAuthor to the allEntries list
            allEntries.addAll(((MailMapAuthorTreeNode) node).getMailMapAuthor().toEntries());
         }
      }

      // Return the list of all entries
      return allEntries;
   }

}
