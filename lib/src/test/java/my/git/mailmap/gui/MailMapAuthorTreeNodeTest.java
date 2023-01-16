package my.git.mailmap.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import javax.swing.tree.MutableTreeNode;

import org.junit.Before;
import org.junit.Test;

import my.git.mailmap.Author;
import my.git.mailmap.MailMapAuthor;

public class MailMapAuthorTreeNodeTest {

   private MailMapAuthorTreeNode node;
   private MailMapAuthor mailMapAuthor;
   private Author primaryAuthor;
   private Author altAuthor1;
   private Author altAuthor2;

   @Before
   public void setUp() {
      primaryAuthor = new Author("John Doe", "john.doe@example.com");
      altAuthor1 = new Author("Jane Doe", "jane.doe@example.com");
      altAuthor2 = new Author("Bob Smith", "bob.smith@example.com");
      ArrayList<Author> altAuthors = new ArrayList<>();
      altAuthors.add(altAuthor1);
      altAuthors.add(altAuthor2);
      mailMapAuthor = new MailMapAuthor(primaryAuthor);
      mailMapAuthor.addAlternativeAuthor(altAuthor1);
      mailMapAuthor.addAlternativeAuthor(altAuthor2);
      node = new MailMapAuthorTreeNode(mailMapAuthor);
   }

   @Test
   public void testAddChild() {
      MutableTreeNode child = new AuthorTreeNode(altAuthor1);
      node.addChild(child);
      assertEquals(3, node.getChildCount());
      assertEquals(altAuthor1, ((AuthorTreeNode) node.getChildAt(2)).getAuthor());
   }

   @Test
   public void testGetChildAt() {
      assertEquals(altAuthor1, ((AuthorTreeNode) node.getChildAt(0)).getAuthor());
   }

   @Test
   public void testGetChildCount() {
      assertEquals(2, node.getChildCount());
   }

   @Test
   public void testGetIndex() {
      MutableTreeNode child = new AuthorTreeNode(altAuthor1);
      assertEquals(0, node.getIndex(child));
   }

   @Test
   public void testGetMailMapAuthor() {
      assertEquals(mailMapAuthor, node.getMailMapAuthor());
   }

   @Test
   public void testIsLeaf() {
      assertFalse(node.isLeaf());
   }

   @Test
   public void testRemove() {
      node.remove(0);
      assertEquals(1, node.getChildCount());
   }

   @Test
   public void testRemoveFromParent() {
      MutableTreeNode child = new AuthorTreeNode(altAuthor1);
      node.addChild(child);
      child.removeFromParent();
      assertEquals(2, node.getChildCount());
   }

   @Test
   public void testSetParent() {
      MutableTreeNode newParent = new MailMapAuthorTreeNode(mailMapAuthor);
      node.setParent(newParent);
      assertEquals(newParent, node.getParent());
   }

   @Test
   public void testToString() {
      assertEquals(primaryAuthor.toString(), node.toString());
   }

}
