package my.git.mailmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The MailMapEntry class represents an entry in a mailmap file. A mailmap entry
 * consists of a primary author, and an optional secondary author. The primary
 * author is represented by a name and an email address, while the secondary
 * author is also represented by a name and email address. The MailMapEntry
 * class provides methods to parse a string representation of a mailmap entry,
 * as well as methods to access the primary and secondary authors.
 */

public class MailMapEntry {

   /**
    * Parses a string as a mailmap entry and returns a {@link MailMapEntry} object.
    * The string should be in the format "Proper Name <Proper Email> Commit Name
    * <Commit Email>". The Proper Name, Commit Name and Commit Email are all
    * optional. The Proper Email should be in the format of an email address.
    * 
    * @param entry a string representing a mailmap entry.
    * @return a {@link MailMapEntry} object representing the parsed entry. Returns
    *         null if the entry is empty or malformed.
    */
   public static MailMapEntry parseEntry(String entry) {
      String beforeRemark = entry.split("#")[0];

      if (beforeRemark == null || beforeRemark.trim().isEmpty()) {
         return null;
      }

      String regex = "\\s*(.*?)?\\s*<([^>]+)>\\s*(?:(.*?)\\s*<([^>]+)?>)?\\s*";

      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(beforeRemark);

      if (matcher.find()) {
         String properName = matcher.group(1);
         String properEmail = matcher.group(2);
         Author primaryAuthor = new Author(properName, properEmail);

         String commitName = matcher.group(3);
         String commitEmail = matcher.group(4);
         Author secondaryAuthor = null;
         if (commitName != null || commitEmail != null) {
            secondaryAuthor = new Author(commitName, commitEmail);
         }

         return new MailMapEntry(primaryAuthor, secondaryAuthor);
      }

      return null;
   }

   /**
    * Reads a mailmap file and returns a list of {@link MailMapEntry} objects.
    * 
    * @param fileName The name of the mailmap file to read.
    * @return a list of {@link MailMapEntry} objects representing the entries in
    *         the mailmap file.
    * @throws IOException if there is an error reading the file.
    */
   public static List<MailMapEntry> readMailMap(String fileName) throws IOException {
      List<MailMapEntry> entries = new ArrayList<>();
      try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
         String line;
         while ((line = reader.readLine()) != null) {
            MailMapEntry entry = MailMapEntry.parseEntry(line);
            if (entry != null) {
               entries.add(entry);
            }
         }
      }
      return entries;
   }

   /**
    * Writes a ".mailmap" file based on the contents of a List of MailMapEntry
    * objects, using their toString method to serialize them.
    *
    * @param fileName the name of the file to be written
    * @param entries  the list of MailMapEntry objects to be written to the file
    * @throws IOException if there is a problem writing the file
    */
   public static void writeMailMap(String fileName, List<MailMapEntry> entries) throws IOException {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
         for (MailMapEntry entry : entries) {
            writer.write(entry.toString());
            writer.newLine();
         }
      }
   }

   private final Author primary;
   private final Author secondary;

   /**
    * Creates a new {@link MailMapEntry} object with the given primary and
    * secondary authors.
    * 
    * @param primary   The primary author of the entry
    * @param secondary The secondary author of the entry. Can be null.
    */
   public MailMapEntry(Author primary, Author secondary) {
      this.primary = primary;
      this.secondary = secondary;
   }

   /**
    * Returns the primary author of this mailmap entry.
    * 
    * @return the primary author.
    */
   public Author getPrimary() {
      return primary;
   }

   /**
    * Returns the secondary author of this mailmap entry.
    * 
    * @return the secondary author. Can be null if there is no secondary author.
    */
   public Author getSecondary() {
      return secondary;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append(primary.getName() != null ? primary.getName() : "");
      sb.append(primary.getEmail() != null ? " <" + primary.getEmail() + ">" : "");
      sb.append(" ");
      if (secondary != null) {
         sb.append(secondary.getName() != null ? secondary.getName() : "");
         sb.append(" ");
         sb.append(secondary.getEmail() != null ? "<" + secondary.getEmail() + ">" : "");
      }

      return sb.toString();
   }

}
