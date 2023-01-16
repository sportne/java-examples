package my.git.mailmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import my.git.mailmap.gui.MailMapAuthorTreeNode;
import my.git.mailmap.gui.MailMapAuthorTreeRoot;
import my.git.mailmap.gui.SortedListModel;

public class AddPrimaryAuthor implements ActionListener {

   private JList<Author> unassignedAuthorsList;
   private Set<Author> unassignedAuthorsSet;

   private MailMapAuthorTreeRoot root;
   private JTree mailmapTree;
   private DefaultTreeModel treeModel;
   private Set<Author> primaryAuthorSet;

   private JFrame parent;

   /**
    * Constructor for testing.
    *
    * @param unassignedAuthorsList
    * @param unassignedAuthorsSet
    * @param root
    * @param mailmapTree
    * @param treeModel
    * @param primaryAuthorSet
    */
   AddPrimaryAuthor(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
         MailMapAuthorTreeRoot root, JTree mailmapTree, DefaultTreeModel treeModel,
         Set<Author> primaryAuthorSet) {
      this(unassignedAuthorsList, unassignedAuthorsSet, root, mailmapTree, treeModel,
            primaryAuthorSet, null);
   }

   /**
    * @param unassignedAuthorsList
    * @param unassignedAuthorsSet
    * @param root
    * @param mailmapTree
    * @param treeModel
    * @param primaryAuthorSet
    * @param parent
    */
   public AddPrimaryAuthor(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
         MailMapAuthorTreeRoot root, JTree mailmapTree, DefaultTreeModel treeModel,
         Set<Author> primaryAuthorSet, JFrame parent) {
      this.unassignedAuthorsList = unassignedAuthorsList;
      this.unassignedAuthorsSet = unassignedAuthorsSet;
      this.root = root;
      this.mailmapTree = mailmapTree;
      this.treeModel = treeModel;
      this.primaryAuthorSet = primaryAuthorSet;
      this.parent = parent;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // Retrieve selected author from unassignedAuthorsList
      Author selectedAuthor = unassignedAuthorsList.getSelectedValue();
      if (selectedAuthor != null) {
         makePrimaryAddition(selectedAuthor);
      }
   }

   /**
    * This method adds an author to the primaryAuthorsList, removes the selected
    * author from the unassignedAuthorsList and unassignedAuthorsSet and adds it to
    * the primaryAuthorSet
    *
    * @param selectedAuthor the author that is to be added as a primary author
    */
   private void makePrimaryAddition(Author selectedAuthor) {
      // Remove the selected author from the unassignedAuthorsList
      ((SortedListModel<Author>) (unassignedAuthorsList.getModel())).removeElement(selectedAuthor);
      unassignedAuthorsSet.remove(selectedAuthor);
      if (!primaryAuthorSet.contains(selectedAuthor)) {
         // Add the selected author as a primary author to the primaryAuthorsList
         MailMapAuthorTreeNode node = new MailMapAuthorTreeNode(new MailMapAuthor(selectedAuthor),
               root);
         treeModel.insertNodeInto(node, root, root.getChildCount());
         treeModel.nodesChanged(root, IntStream.range(0, root.getChildCount()).toArray());
         if (root.getChildCount() > 0) {
            mailmapTree.expandPath(new TreePath(new Object[] { root }));
         }
         primaryAuthorSet.add(selectedAuthor);
      } else {
         JOptionPane.showMessageDialog(parent,
               "The selected author already exists as a primary author, removing from the unassigned list.");
      }
   }

}
