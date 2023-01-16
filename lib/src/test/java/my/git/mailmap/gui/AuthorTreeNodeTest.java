package my.git.mailmap.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.junit.Test;

import my.git.mailmap.Author;

public class AuthorTreeNodeTest {

   @Test
   public void testChildren() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertFalse(node.children().hasMoreElements());
   }

   @Test
   public void testGetAllowsChildren() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertFalse(node.getAllowsChildren());
   }

   @Test
   public void testGetAuthor() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertEquals(author, node.getAuthor());
   }

   @Test
   public void testGetChildAt() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertNull(node.getChildAt(0));
   }

   @Test
   public void testGetChildCount() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertEquals(0, node.getChildCount());
   }

   @Test
   public void testGetIndex() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertEquals(-1, node.getIndex(node));
   }

   @Test
   public void testGetParent() {
      Author author = new Author("John Doe", "john.doe@example.com");
      MutableTreeNode parent = new AuthorTreeNode(author);
      AuthorTreeNode node = new AuthorTreeNode(author, parent);
      assertEquals(parent, node.getParent());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testInsert() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      node.insert(new AuthorTreeNode(author), 0);
   }

   @Test
   public void testIsLeaf() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertTrue(node.isLeaf());
   }

   @Test
   public void testRemoveFromParent() {
      Author author = new Author("John Doe", "john.doe@example.com");
      DefaultMutableTreeNode parent = new DefaultMutableTreeNode();
      AuthorTreeNode node = new AuthorTreeNode(author);
      parent.add(node);
      node.removeFromParent();
      assertNull(node.getParent());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testRemoveInt() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      node.remove(0);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testRemoveMutableTreeNode() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      node.remove(new AuthorTreeNode(author));
   }

   @Test
   public void testSetParent() {
      Author author = new Author("John Doe", "john.doe@example.com");
      MutableTreeNode parent = new AuthorTreeNode(author);
      AuthorTreeNode node = new AuthorTreeNode(author);
      node.setParent(parent);
      assertEquals(parent, node.getParent());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testSetUserObject() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      node.setUserObject("test");
   }

   @Test
   public void testToString() {
      Author author = new Author("John Doe", "john.doe@example.com");
      AuthorTreeNode node = new AuthorTreeNode(author);
      assertEquals(author.toString(), node.toString());
   }
}