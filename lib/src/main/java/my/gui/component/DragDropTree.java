package my.gui.component;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.TransferHandler;

/**
 * A class that extends JTree to provide drag and drop functionality.
 */
public class DragDropTree extends JTree {

   private static final long serialVersionUID = -4220690137703930843L;

   /**
    * Constructs a new DragDropTree.
    */
   public DragDropTree() {
      setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Root")));
      getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
      setTransferHandler(new TreeTransferHandler());
   }

   /**
    * A class that handles the transfer of data to and from the tree.
    */
   public class TreeTransferHandler extends TransferHandler {

      private static final long serialVersionUID = 2342159037724800901L;

      /**
       * Checks if the data being dragged can be dropped into the tree.
       *
       * @param support the TransferSupport object containing information about the
       *                drag and drop action
       * @return true if the data can be imported, false otherwise
       */
      @Override
      public boolean canImport(TransferSupport support) {
         return support.isDataFlavorSupported(DataFlavor.stringFlavor);
      }

      /**
       * Handles the insertion of the dragged data into the tree.
       *
       * @param support the TransferSupport object containing information about the
       *                drag and drop action
       * @return true if the data was imported successfully, false otherwise
       */
      @Override
      public boolean importData(TransferSupport support) {
         if (!canImport(support)) {
            return false;
         }

         MutableTreeNode parent;
         MutableTreeNode child;

         try {
            Transferable t = support.getTransferable();
            Object value = t.getTransferData(DataFlavor.stringFlavor);
            TreePath path = getSelectionPath();

            if (path == null) {
               parent = (MutableTreeNode) getModel().getRoot();
            } else {
               parent = (MutableTreeNode) path.getLastPathComponent();
            }

            child = new DefaultMutableTreeNode(value);
            ((DefaultTreeModel) getModel()).insertNodeInto(child, parent, parent.getChildCount());

            return true;
         } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
         }

         return false;
      }
   }
}
