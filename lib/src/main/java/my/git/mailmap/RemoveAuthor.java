package my.git.mailmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import my.git.mailmap.gui.AuthorTreeNode;
import my.git.mailmap.gui.MailMapAuthorTreeNode;
import my.git.mailmap.gui.SortedListModel;

public class RemoveAuthor implements ActionListener {

   private JList<Author> unassignedAuthorsList;

   private JTree mailmapTree;
   private DefaultTreeModel treeModel;
   private Set<Author> primaryAuthorSet;

   /**
    * @param unassignedAuthorsList
    * @param mailmapTree
    * @param treeModel
    * @param primaryAuthorSet
    */
   public RemoveAuthor(JList<Author> unassignedAuthorsList, JTree mailmapTree,
         DefaultTreeModel treeModel, Set<Author> primaryAuthorSet) {
      this.unassignedAuthorsList = unassignedAuthorsList;
      this.mailmapTree = mailmapTree;
      this.treeModel = treeModel;
      this.primaryAuthorSet = primaryAuthorSet;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object selectedObj = mailmapTree.getLastSelectedPathComponent();
      if (selectedObj instanceof AuthorTreeNode) {
         AuthorTreeNode node = (AuthorTreeNode) selectedObj;
         treeModel.removeNodeFromParent(node);
         SortedListModel<Author> unassignedModel = (SortedListModel<Author>) (unassignedAuthorsList
               .getModel());
         unassignedModel.addElement(node.getAuthor());
      } else if (selectedObj instanceof MailMapAuthorTreeNode) {
         MailMapAuthorTreeNode node = (MailMapAuthorTreeNode) selectedObj;
         treeModel.removeNodeFromParent(node);
         SortedListModel<Author> unassignedModel = (SortedListModel<Author>) (unassignedAuthorsList
               .getModel());
         unassignedModel.addElement(node.getMailMapAuthor().getPrimaryAuthor());
         for (Author alt : node.getMailMapAuthor().getAlternativeAuthors()) {
            unassignedModel.addElement(alt);
         }
         primaryAuthorSet.remove(node.getMailMapAuthor().getPrimaryAuthor());
      }
   }

}
