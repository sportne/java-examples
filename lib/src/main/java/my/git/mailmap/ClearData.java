package my.git.mailmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JList;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import my.git.mailmap.gui.MailMapAuthorTreeRoot;
import my.git.mailmap.gui.SortedListModel;

public class ClearData implements ActionListener {

   private JList<Author> unassignedAuthorsList;
   private Set<Author> unassignedAuthorsSet;

   private MailMapAuthorTreeRoot root;
   private DefaultTreeModel treeModel;
   private Set<Author> primaryAuthorSet;

   public ClearData(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
         MailMapAuthorTreeRoot root, DefaultTreeModel treeModel, Set<Author> primaryAuthorSet) {
      this.unassignedAuthorsList = unassignedAuthorsList;
      this.unassignedAuthorsSet = unassignedAuthorsSet;
      this.root = root;
      this.treeModel = treeModel;
      this.primaryAuthorSet = primaryAuthorSet;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      SortedListModel<Author> unassignedModel = (SortedListModel<Author>) (unassignedAuthorsList
            .getModel());
      unassignedModel.clear();
      unassignedAuthorsSet.clear();

      List<MutableTreeNode> rootChildren = new ArrayList<>(root.getChildren());
      for (MutableTreeNode node : rootChildren) {
         treeModel.removeNodeFromParent(node);
      }
      primaryAuthorSet.clear();
   }

}
