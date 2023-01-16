package my.git.mailmap.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import my.git.mailmap.Author;
import my.git.mailmap.MailMapAuthor;

public class MailMapAuthorTreeNode implements MutableTreeNode {
   private final MailMapAuthor mailMapAuthor;
   private final List<MutableTreeNode> children;

   private MutableTreeNode parent;
   private Comparator<TreeNode> comparator = (o1, o2) -> o1.toString()
         .compareToIgnoreCase(o2.toString());

   public MailMapAuthorTreeNode(MailMapAuthor mailMapAuthor) {
      this(mailMapAuthor, null);
   }

   public MailMapAuthorTreeNode(MailMapAuthor mailMapAuthor, MutableTreeNode parent) {
      this.mailMapAuthor = mailMapAuthor;
      this.parent = parent;
      this.children = new ArrayList<>();
      for (Author alt : mailMapAuthor.getAlternativeAuthors()) {
         children.add(new AuthorTreeNode(alt));
      }
   }

   public void addChild(MutableTreeNode child) {
      children.add(child);
      child.setParent(this);
      children.sort(comparator);
   }

   @Override
   public Enumeration<? extends TreeNode> children() {
      return Collections.enumeration(children);
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

   @Override
   public int getIndex(TreeNode node) {
      return children.indexOf(node);
   }

   public MailMapAuthor getMailMapAuthor() {
      return mailMapAuthor;
   }

   @Override
   public TreeNode getParent() {
      return parent;
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
      parent.remove(this);
   }

   @Override
   public void setParent(MutableTreeNode newParent) {
      if (parent != null) {
         removeFromParent();
      }
      this.parent = newParent;
   }

   @Override
   public void setUserObject(Object object) {
      throw new UnsupportedOperationException();
   }

   @Override
   public String toString() {
      return mailMapAuthor.getPrimaryAuthor().toString();
   }

}
