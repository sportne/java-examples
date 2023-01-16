package my.git.mailmap.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import my.git.mailmap.Author;

public class AuthorTreeNode implements TransferableNode {
   private final Author author;
   private MutableTreeNode parent;

   public AuthorTreeNode(Author author) {
      this(author, null);
   }

   public AuthorTreeNode(Author author, MutableTreeNode parent) {
      this.parent = parent;
      this.author = author;
   }

   @Override
   public Enumeration<? extends TreeNode> children() {
      return Collections.emptyEnumeration();
   }

   @Override
   public boolean getAllowsChildren() {
      return false;
   }

   public Author getAuthor() {
      return author;
   }

   @Override
   public TreeNode getChildAt(int childIndex) {
      return null; // author node has no children
   }

   @Override
   public int getChildCount() {
      return 0;
   }

   @Override
   public int getIndex(TreeNode node) {
      return -1; // author node has no children
   }

   @Override
   public TreeNode getParent() {
      return parent;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (flavor.equals(DataFlavor.stringFlavor)) {
         return author.toString();
      } else {
         throw new UnsupportedFlavorException(flavor);
      }
   }

   // Implement the methods of the Transferable interface
   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { DataFlavor.stringFlavor };
   }

   @Override
   public void insert(MutableTreeNode child, int index) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(DataFlavor.stringFlavor);
   }

   @Override
   public boolean isLeaf() {
      return true;
   }

   @Override
   public void remove(int index) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void remove(MutableTreeNode node) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void removeFromParent() {
      parent.remove(this);
   }

   @Override
   public void setParent(MutableTreeNode newParent) {
      this.parent = newParent;
   }

   @Override
   public void setUserObject(Object object) {
      throw new UnsupportedOperationException();
   }

   @Override
   public String toString() {
      return author.toString();
   }
}
