package my.git.mailmap.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.tree.MutableTreeNode;

import org.junit.Before;
import org.junit.Test;

import my.git.mailmap.Author;
import my.git.mailmap.MailMapAuthor;

public class MailMapAuthorTreeRootTest {

   private Author author;
   
   @Before
   public void setup()
   {
      author = new Author("John Q Public", "jqp@public.com");
   }
   
   @Test
   public void testAddChild() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child2 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      root.addChild(child2);
      assertEquals(2, root.getChildCount());
      assertEquals(child1, root.getChildAt(0));
      assertEquals(child2, root.getChildAt(1));
   }

   @Test
   public void testAddChildren() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child2 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child3 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      Collection<MutableTreeNode> children = new ArrayList<>();
      children.add(child1);
      children.add(child2);
      children.add(child3);
      root.addChildren(children);
      assertEquals(3, root.getChildCount());
      assertEquals(child1, root.getChildAt(0));
      assertEquals(child2, root.getChildAt(1));
      assertEquals(child3, root.getChildAt(2));
   }

   @Test
   public void testClear() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child2 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      root.addChild(child2);
      root.clear();

      assertEquals(0, root.getChildCount());

   }

   @Test
   public void testGetAllowsChildren() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      assertTrue(root.getAllowsChildren());
   }

   @Test
   public void testGetIndex() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child2 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      root.addChild(child2);
      assertEquals(0, root.getIndex(child1));
      assertEquals(1, root.getIndex(child2));
   }

   @Test
   public void testGetParent() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      assertNull(root.getParent());
   }

   @Test
   public void testInsert() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child2 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child3 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      root.insert(child2, 0);
      root.insert(child3, 1);
      assertEquals(3, root.getChildCount());
      assertEquals(child2, root.getChildAt(0));
      assertEquals(child3, root.getChildAt(1));
      assertEquals(child1, root.getChildAt(2));
   }

   @Test
   public void testIsLeaf() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      assertTrue(root.isLeaf());
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      assertFalse(root.isLeaf());
   }

   @Test
   public void testRemove() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      MutableTreeNode child2 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      root.addChild(child2);
      root.remove(0);
      assertEquals(1, root.getChildCount());
      assertEquals(child2, root.getChildAt(0));

      root.remove(child2);
      assertEquals(0, root.getChildCount());
   }

   @Test
   public void testRemoveFromParent() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      child1.removeFromParent();
      assertEquals(0, root.getChildCount());
   }

   @Test
   public void testSetParent() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      MutableTreeNode child1 = new MailMapAuthorTreeNode(new MailMapAuthor(author));
      root.addChild(child1);
      child1.setParent(null);
      assertEquals(0, root.getChildCount());
   }

   @Test
   public void testSetUserObject() {
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      root.setUserObject(null);
      assertEquals("Git Authors", root.toString());
   }

}
