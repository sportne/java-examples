package my.git.mailmap.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import my.git.mailmap.Author;
import my.git.mailmap.MailMapAuthor;

public class MailMapAuthorTreeRoot implements MutableTreeNode, DropTargetListener {

   private final List<MutableTreeNode> children;
   private final Comparator<MutableTreeNode> comparator = (o1, o2) -> o1.toString()
         .compareToIgnoreCase(o2.toString());

   public MailMapAuthorTreeRoot() {
      children = new ArrayList<>();
   }

   public void addChild(MutableTreeNode child) {
      children.add(child);
      children.sort(comparator);
   }

   public void addChildren(Collection<MutableTreeNode> newChildren) {
      children.addAll(newChildren);
      children.sort(comparator);
   }

   @Override
   public Enumeration<? extends TreeNode> children() {
      return Collections.enumeration(children);
   }

   public void clear() {
      children.clear();
   }

   @Override
   public void dragEnter(DropTargetDragEvent dtde) {
      // do nothing
   }

   @Override
   public void dragExit(DropTargetEvent dte) {
      // do nothing
   }

   @Override
   public void dragOver(DropTargetDragEvent dtde) {
      // do nothing
   }

   @Override
   public void drop(DropTargetDropEvent dtde) {
      Transferable transferable = dtde.getTransferable();
      if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
         try {
            // Accept the drop and get the dropped data
            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            String data = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            // Create a new child and add it
            addChild(new MailMapAuthorTreeNode(new MailMapAuthor(Author.parse(data))));
         } catch (Exception e) {
            dtde.rejectDrop();
         }
      } else {
         // Reject the drop if the data flavor is not supported
         dtde.rejectDrop();
      }

   }

   @Override
   public void dropActionChanged(DropTargetDragEvent dtde) {
      // do nothing
   }

   @Override
   public boolean getAllowsChildren() {
      return true;
   }

   @Override
   public TreeNode getChildAt(int childIndex) {
      return children.get(childIndex);
   }

   @Override
   public int getChildCount() {
      return children.size();
   }

   public List<MutableTreeNode> getChildren() {
      return children;
   }

   @Override
   public int getIndex(TreeNode node) {
      for (int i = 0; i < children.size(); i++) {
         TreeNode child = children.get(i);
         if (child == node) {
            return i;
         }
      }
      return -1;
   }

   @Override
   public TreeNode getParent() {
      return null;
   }

   @Override
   public void insert(MutableTreeNode child, int index) {
      children.add(index, child);
      children.sort(comparator);
   }

   @Override
   public boolean isLeaf() {
      return children.isEmpty();
   }

   @Override
   public void remove(int index) {
      children.remove(index);
   }

   @Override
   public void remove(MutableTreeNode node) {
      children.remove(node);
   }

   @Override
   public void removeFromParent() {
      // do nothing, this is a root
   }

   @Override
   public void setParent(MutableTreeNode newParent) {
      // do nothing, this is a root
   }

   @Override
   public void setUserObject(Object object) {
      // do nothing, there is no user object
   }
   
   @Override
   public String toString()
   {
      return "Git Authors";
   }

}
