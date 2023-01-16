package my.git.mailmap;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class MailMapAuthorTest {

   @Test
   public void testAddAlternativeAuthor() {
      MailMapAuthor mailMapAuthor = new MailMapAuthor("John Doe", "johndoe@example.com");
      mailMapAuthor.addAlternativeAuthor("Jane Smith", "janesmith@example.com");
      assertEquals(1, mailMapAuthor.getAlternativeAuthors().size());
      assertEquals("Jane Smith", mailMapAuthor.getAlternativeAuthors().get(0).getName());
      assertEquals("janesmith@example.com",
            mailMapAuthor.getAlternativeAuthors().get(0).getEmail());
   }

   @Test
   public void testAddAlternativeAuthorWithAuthorObject() {
      MailMapAuthor mailMapAuthor = new MailMapAuthor("John Doe", "johndoe@example.com");
      Author alternativeAuthor = new Author("Jane Smith", "janesmith@example.com");
      mailMapAuthor.addAlternativeAuthor(alternativeAuthor);
      assertEquals(1, mailMapAuthor.getAlternativeAuthors().size());
      assertEquals("Jane Smith", mailMapAuthor.getAlternativeAuthors().get(0).getName());
      assertEquals("janesmith@example.com",
            mailMapAuthor.getAlternativeAuthors().get(0).getEmail());
   }

   @Test
   public void testMailMapAuthor() {
      MailMapAuthor mailMapAuthor = new MailMapAuthor("John Doe", "johndoe@example.com");
      assertEquals("John Doe", mailMapAuthor.getPrimaryAuthor().getName());
      assertEquals("johndoe@example.com", mailMapAuthor.getPrimaryAuthor().getEmail());
      assertEquals(0, mailMapAuthor.getAlternativeAuthors().size());
   }

   @Test
   public void testMailMapAuthorWithAuthorObject() {
      Author primaryAuthor = new Author("John Doe", "johndoe@example.com");
      MailMapAuthor mailMapAuthor = new MailMapAuthor(primaryAuthor);
      assertEquals("John Doe", mailMapAuthor.getPrimaryAuthor().getName());
      assertEquals("johndoe@example.com", mailMapAuthor.getPrimaryAuthor().getEmail());
      assertEquals(0, mailMapAuthor.getAlternativeAuthors().size());
   }

   @Test
   public void testToEntries() {
      MailMapAuthor mailMapAuthor = new MailMapAuthor("John Doe", "johndoe@example.com");
      mailMapAuthor.addAlternativeAuthor("Jane Smith", "janesmith@example.com");
      List<MailMapEntry> entries = mailMapAuthor.toEntries();
      assertEquals(2, entries.size());
      assertEquals("John Doe", entries.get(0).getPrimary().getName());
      assertEquals("johndoe@example.com", entries.get(0).getPrimary().getEmail());
      assertEquals(null, entries.get(0).getSecondary());
      assertEquals("John Doe", entries.get(1).getPrimary().getName());
      assertEquals("johndoe@example.com", entries.get(1).getPrimary().getEmail());
      assertEquals("Jane Smith", entries.get(1).getSecondary().getName());
      assertEquals("janesmith@example.com", entries.get(1).getSecondary().getEmail());
   }

   @Test
   public void testToString() {
      MailMapAuthor mailMapAuthor = new MailMapAuthor("John Doe", "johndoe@example.com");
      mailMapAuthor.addAlternativeAuthor("Jane Smith", "janesmith@example.com");
      assertEquals("John Doe, johndoe@example.com\n  Jane Smith, janesmith@example.com",
            mailMapAuthor.toString());
   }
}
