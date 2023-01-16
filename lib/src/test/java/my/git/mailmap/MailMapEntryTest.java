package my.git.mailmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MailMapEntryTest {

	@Test
	public void parseEntry_primaryAndSecondaryEmailOnly_returnsCorrectMailMapEntry() {
		String entry = " <primary@example.com>  <secondary@example.com>";
		MailMapEntry mailMapEntry = MailMapEntry.parseEntry(entry);
		assertNotNull(mailMapEntry);
		assertEquals("primary@example.com", mailMapEntry.getPrimary().getEmail());
		assertEquals("", mailMapEntry.getPrimary().getName());
		assertEquals("secondary@example.com", mailMapEntry.getSecondary().getEmail());
		assertEquals("", mailMapEntry.getSecondary().getName());
	}

	@Test
	public void testParseEntry_withEmptyString() {
		String entry = "  # comment";
		MailMapEntry mailMapEntry = MailMapEntry.parseEntry(entry);
		assertNull(mailMapEntry);
	}

	@Test
	public void testParseEntry_withPrimaryAndSecondaryAuthors() {
		String entry = "John Doe <johndoe@example.com> Jane Doe <janedoe@example.com> # comment";
		MailMapEntry mailMapEntry = MailMapEntry.parseEntry(entry);
		assertEquals("John Doe", mailMapEntry.getPrimary().getName());
		assertEquals("johndoe@example.com", mailMapEntry.getPrimary().getEmail());
		assertEquals("Jane Doe", mailMapEntry.getSecondary().getName());
		assertEquals("janedoe@example.com", mailMapEntry.getSecondary().getEmail());
	}

	@Test
	public void testParseEntry_withPrimaryAuthorEmailOnly() {
		String entry = " <johndoe@example.com>  # comment";
		MailMapEntry mailMapEntry = MailMapEntry.parseEntry(entry);
		assertEquals("", mailMapEntry.getPrimary().getName());
		assertEquals("johndoe@example.com", mailMapEntry.getPrimary().getEmail());
		assertNull(mailMapEntry.getSecondary());
	}

	@Test
	public void testParseEntry_withPrimaryAuthorOnly() {
		String entry = "John Doe <johndoe@example.com>  # comment";
		MailMapEntry mailMapEntry = MailMapEntry.parseEntry(entry);
		assertEquals("John Doe", mailMapEntry.getPrimary().getName());
		assertEquals("johndoe@example.com", mailMapEntry.getPrimary().getEmail());
		assertNull(mailMapEntry.getSecondary());
	}

	@Test
	public void testToString() {
		MailMapEntry entry = new MailMapEntry(new Author("John Doe", "john.doe@example.com"),
				new Author("Jane Doe", "jane.doe@example.com"));
		String expectedString = "John Doe <john.doe@example.com> Jane Doe <jane.doe@example.com>";

		assertEquals(expectedString, entry.toString());
	}

	@Test
	public void testToString_withPrimaryAndSecondaryAuthors() {
		Author primaryAuthor = new Author("John Doe", "johndoe@example.com");
		Author secondaryAuthor = new Author("Jane Doe", "janedoe@example.com");
		MailMapEntry mailMapEntry = new MailMapEntry(primaryAuthor, secondaryAuthor);
		assertEquals("John Doe <johndoe@example.com> Jane Doe <janedoe@example.com>",
				mailMapEntry.toString());
	}

	@Test
	public void testToString_withPrimaryAuthorEmailOnly() {
		Author primaryAuthor = new Author("", "johndoe@example.com");
		Author secondaryAuthor = null;
		MailMapEntry mailMapEntry = new MailMapEntry(primaryAuthor, secondaryAuthor);
		assertEquals(" <johndoe@example.com>".trim(), mailMapEntry.toString().trim());
	}
}
