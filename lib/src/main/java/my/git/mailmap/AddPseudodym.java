package my.git.mailmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import my.git.mailmap.gui.AuthorTreeNode;
import my.git.mailmap.gui.MailMapAuthorTreeNode;
import my.git.mailmap.gui.MailMapAuthorTreeRoot;
import my.git.mailmap.gui.SortedListModel;

/**
 * The AddPseudodym class is an ActionListener for adding an alternative author
 * name to the MailMapAuthorTree. It takes in a JList of unassigned authors, a
 * set of unassigned authors, a MailMapAuthorTreeRoot, a JTree, a
 * DefaultTreeModel, and a JButton for adding alternative authors. The
 * actionPerformed method retrieves the selected primary author from the
 * MailMapAuthorTree and the selected unassigned author from the JList and adds
 * the unassigned author as an alternative name to the selected primary author.
 */
public class AddPseudodym implements ActionListener {

   private JList<Author> unassignedAuthorsList;
   private Set<Author> unassignedAuthorsSet;

   private MailMapAuthorTreeRoot root;
   private JTree mailmapTree;
   private DefaultTreeModel treeModel;

   private JButton addAlternativeAuthorButton;

   public AddPseudodym(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
         MailMapAuthorTreeRoot root, JTree mailmapTree, DefaultTreeModel treeModel,
         JButton addAlternativeAuthorButton) {
      this.unassignedAuthorsList = unassignedAuthorsList;
      this.unassignedAuthorsSet = unassignedAuthorsSet;
      this.root = root;
      this.mailmapTree = mailmapTree;
      this.treeModel = treeModel;
      this.addAlternativeAuthorButton = addAlternativeAuthorButton;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // Retrieve selected author from unassignedAuthorsList
      Author selectedAuthor = unassignedAuthorsList.getSelectedValue();
      if (selectedAuthor != null) {
         MailMapAuthor selectedPrimaryAuthor = getSelectedPrimaryAuthor();
         if (selectedPrimaryAuthor != null) {
            makeSecondaryAddition(selectedAuthor, selectedPrimaryAuthor);
         } else {
            JOptionPane.showMessageDialog(addAlternativeAuthorButton,
                  "To add an alternative Author name, a primary author must be selected to add it to.");
         }
      }
   }

   /**
    * Finds the MailMapAuthorTreeNode that corresponds to the given Author.
    *
    * @param author The Author to find the corresponding MailMapAuthorTreeNode for.
    * @return The MailMapAuthorTreeNode that corresponds to the given Author, or
    *         null if none is found.
    */
   private MailMapAuthorTreeNode find(Author author) {

      // iterate through the children of the root
      for (TreeNode node : root.getChildren()) {
         // check if the current node is a MailMapAuthorTreeNode and if its primary
         // author matches the given author
         if ((node instanceof MailMapAuthorTreeNode) && ((MailMapAuthorTreeNode) node)
               .getMailMapAuthor().getPrimaryAuthor().equals(author)) {
            // if a match is found, return the node
            return (MailMapAuthorTreeNode) node;
         }
      }

      // if no match is found, return null
      return null;
   }

   /**
    * Returns the selected primary author in the MailMapAuthorTree.
    *
    * @return the selected primary author, or null if no author is selected
    */
   private MailMapAuthor getSelectedPrimaryAuthor() {
      Object selectedObj = mailmapTree.getLastSelectedPathComponent();
      if (selectedObj == null) {
         return null;
      } else if (selectedObj instanceof AuthorTreeNode) {
         MailMapAuthorTreeNode node = (MailMapAuthorTreeNode) (((AuthorTreeNode) selectedObj)
               .getParent());
         return node.getMailMapAuthor();
      } else if (selectedObj instanceof MailMapAuthorTreeNode) {
         return ((MailMapAuthorTreeNode) selectedObj).getMailMapAuthor();
      }
      return null;
   }

   /**
    * Makes the secondary addition to the selected primary author in
    * primaryAuthorsList.
    *
    * @param selectedAuthor        the author to be added as secondary
    * @param selectedPrimaryAuthor the primary author to which the secondary author
    *                              is added
    */
   private void makeSecondaryAddition(Author selectedAuthor, MailMapAuthor selectedPrimaryAuthor) {
      // Remove the selected author from the unassignedAuthorsList
      ((SortedListModel<Author>) (unassignedAuthorsList.getModel())).removeElement(selectedAuthor);
      unassignedAuthorsSet.remove(selectedAuthor);
      // Add the selected author as a secondary author to the selected primary author
      // in primaryAuthorsList
      selectedPrimaryAuthor.addAlternativeAuthor(selectedAuthor);
      // Update the selected primary author in primaryAuthorsList
      MailMapAuthorTreeNode node = find(selectedPrimaryAuthor.getPrimaryAuthor());
      if (node != null) {
         treeModel.insertNodeInto(new AuthorTreeNode(selectedAuthor, node), node,
               node.getChildCount());
         treeModel.nodesChanged(node, IntStream.range(0, node.getChildCount()).toArray());
      }
   }

}
