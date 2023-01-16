package my.git.mailmap.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class MailMapAuthorTreeRoot implements MutableTreeNode {

   private final List<MutableTreeNode> children;
   private final Comparator<MutableTreeNode> comparator = (o1, o2) -> o1.toString()
         .compareToIgnoreCase(o2.toString());

   public MailMapAuthorTreeRoot() {
      children = new ArrayList<>();
   }

   public void addChild(MutableTreeNode child) {
      children.add(child);
      child.setParent(this);
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
   public String toString() {
      return "Git Authors";
   }

}
