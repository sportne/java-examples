package my.git.mailmap.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class TreeTransferHandler extends TransferHandler {

   private static final long serialVersionUID = -2268179539471653838L;
   private static final DataFlavor AUTHOR_FLAVOR = new DataFlavor(AuthorTreeNode.class,
         "AuthorTreeNode");
   private static final DataFlavor MAIL_MAP_AUTHOR_FLAVOR = new DataFlavor(
         MailMapAuthorTreeNode.class, "MailMapAuthorTreeNode");

   @Override
   public boolean canImport(TransferSupport support) {
      if (!support.isDrop()) {
         return false;
      }
      if (!support.isDataFlavorSupported(AUTHOR_FLAVOR)
            && !support.isDataFlavorSupported(MAIL_MAP_AUTHOR_FLAVOR)) {
         return false;
      }
      return true;
   }

   @Override
   public boolean importData(TransferSupport support) {
      if (!canImport(support)) {
         return false;
      }

      JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
      DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dl.getPath().getLastPathComponent();
      JTree tree = (JTree) support.getComponent();
      DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

      try {
         Transferable transferable = support.getTransferable();

         MutableTreeNode node;
         if (transferable.isDataFlavorSupported(AUTHOR_FLAVOR)) {
            node = (AuthorTreeNode) transferable.getTransferData(AUTHOR_FLAVOR);
         } else if (transferable.isDataFlavorSupported(MAIL_MAP_AUTHOR_FLAVOR)) {
            node = (MailMapAuthorTreeNode) transferable.getTransferData(MAIL_MAP_AUTHOR_FLAVOR);
         } else {
            return false;
         }

         model.insertNodeInto(node, parent, parent.getChildCount());
         return true;
      } catch (UnsupportedFlavorException | IOException ex) {
         return false;
      }
   }

   @Override
   public int getSourceActions(JComponent c) {
      return MOVE;
   }

   @Override
   protected Transferable createTransferable(JComponent c) {
      JTree tree = (JTree) c;
      TreePath path = tree.getSelectionPath();
      if (path == null) {
         return null;
      }
      Object obj = path.getLastPathComponent();

      if (obj instanceof Transferable) {
         return (Transferable) obj;
      }
      return null;
   }
}
