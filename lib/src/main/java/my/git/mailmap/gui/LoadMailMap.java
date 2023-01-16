package my.git.mailmap.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import my.git.mailmap.Author;
import my.git.mailmap.MailMapAuthor;
import my.git.mailmap.MailMapEntry;

public class LoadMailMap implements ActionListener {

   private JList<Author> unassignedAuthorsList;
   private Set<Author> unassignedAuthorsSet;

   private MailMapAuthorTreeRoot root;
   private JTree mailmapTree;
   private DefaultTreeModel treeModel;
   private Set<Author> primaryAuthorSet;

   private JFrame parent;

   LoadMailMap(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
         MailMapAuthorTreeRoot root, JTree mailmapTree, DefaultTreeModel treeModel,
         Set<Author> primaryAuthorSet) {
      this(unassignedAuthorsList, unassignedAuthorsSet, root, mailmapTree, treeModel,
            primaryAuthorSet, null);
   }

   public LoadMailMap(JList<Author> unassignedAuthorsList, Set<Author> unassignedAuthorsSet,
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
      JFileChooser fileChooser = new JFileChooser();
      int returnVal = fileChooser.showOpenDialog(parent);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         try {
            // Use the MailMapEntry.readMailMap() method to read the selected file
            List<MailMapEntry> mailMapEntries = MailMapEntry
                  .readMailMap(fileChooser.getSelectedFile().getAbsolutePath());
            // Clear the unassignedAuthorsList and primaryAuthorsList
            SortedListModel<Author> unassignedModel = (SortedListModel<Author>) (unassignedAuthorsList
                  .getModel());
            unassignedModel.clear();
            unassignedAuthorsSet.clear();

            List<MutableTreeNode> rootChildren = new ArrayList<>(root.getChildren());
            for (MutableTreeNode node : rootChildren) {
               treeModel.removeNodeFromParent(node);
            }
            primaryAuthorSet.clear();

            // Iterate through the mailMapEntries list and add the primary authors to the
            // primaryAuthorsList and the alternative authors to the unassignedAuthorsList
            for (MailMapEntry entry : mailMapEntries) {
               MailMapAuthorTreeNode treeNode = null;
               if (entry.getPrimary() != null) {
                  if (primaryAuthorSet.contains(entry.getPrimary())) {
                     if (entry.getSecondary() != null) {
                        treeNode = find(entry.getPrimary());
                     }
                  } else {
                     treeNode = new MailMapAuthorTreeNode(new MailMapAuthor(entry.getPrimary()),
                           root);
                     primaryAuthorSet.add(entry.getPrimary());
                     treeModel.insertNodeInto(treeNode, root, root.getChildCount());
                     treeModel.nodesChanged(root,
                           IntStream.range(0, root.getChildCount()).toArray());
                  }

               }
               if (entry.getSecondary() != null) {
                  treeNode.getMailMapAuthor().addAlternativeAuthor(entry.getSecondary());
                  treeModel.insertNodeInto(new AuthorTreeNode(entry.getSecondary(), treeNode),
                        treeNode, treeNode.getChildCount());
                  treeModel.nodesChanged(treeNode,
                        IntStream.range(0, treeNode.getChildCount()).toArray());
               }
            }
            if (root.getChildCount() > 0) {
               mailmapTree.expandPath(new TreePath(new Object[] { root }));
            }
         } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error reading mailmap file.");
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

}
