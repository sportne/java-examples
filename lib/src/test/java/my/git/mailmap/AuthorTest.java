package my.git.mailmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AuthorTest {

   @Test
   public void testAuthorConstructor() {
      Author author = new Author("John Smith", "john.smith@email.com");
      assertEquals("John Smith", author.getName());
      assertEquals("john.smith@email.com", author.getEmail());
   }

   @Test
   public void testAuthorEquals() {
      Author author1 = new Author("John Smith", "john.smith@email.com");
      Author author2 = new Author("John Smith", "john.smith@email.com");
      Author author3 = new Author("Jane Smith", "jane.smith@email.com");
      assertTrue(author1.equals(author2));
      assertFalse(author1.equals(author3));
   }

   @Test
   public void testAuthorHashCode() {
      Author author1 = new Author("John Smith", "john.smith@email.com");
      Author author2 = new Author("John Smith", "john.smith@email.com");
      assertEquals(author1.hashCode(), author2.hashCode());
   }

   @Test
   public void testAuthorParse() {
      String authorString = "John Smith, john.smith@email.com";
      Author author = Author.parse(authorString);
      assertEquals("John Smith", author.getName());
      assertEquals("john.smith@email.com", author.getEmail());

      String invalidAuthorString = "John Smith";
      Author invalidAuthor = Author.parse(invalidAuthorString);
      assertNull(invalidAuthor);
   }

   @Test
   public void testAuthorToString() {
      Author author = new Author("John Smith", "john.smith@email.com");
      assertEquals("John Smith, john.smith@email.com", author.toString());
   }

}
