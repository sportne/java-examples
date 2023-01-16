package my.git.mailmap;

import java.util.ArrayList;
import java.util.List;

public class MailMapAuthor {

   private final Author primaryAuthor;
   private final List<Author> alternativeAuthors;

   public MailMapAuthor(String primaryName, String primaryEmail) {
      this.primaryAuthor = new Author(primaryName, primaryEmail);
      this.alternativeAuthors = new ArrayList<>();
   }

   public MailMapAuthor(Author primary) {
      this.primaryAuthor = primary;
      this.alternativeAuthors = new ArrayList<>();
   }

   public Author getPrimaryAuthor() {
      return primaryAuthor;
   }

   public List<Author> getAlternativeAuthors() {
      return alternativeAuthors;
   }

   public void addAlternativeAuthor(String name, String email) {
      alternativeAuthors.add(new Author(name, email));
   }

   public void addAlternativeAuthor(Author alternativeAuthor) {
      alternativeAuthors.add(alternativeAuthor);
   }

   public List<MailMapEntry> toEntries() {
      List<MailMapEntry> entries = new ArrayList<>();

      entries.add(new MailMapEntry(primaryAuthor, null));
      for (Author alt : alternativeAuthors) {
         entries.add(new MailMapEntry(primaryAuthor, alt));
      }

      return entries;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(primaryAuthor);
      builder.append("\n");
      for(Author alt : alternativeAuthors)
      {
         builder.append("  ");
         builder.append(alt);
      }
      return builder.toString();
   }

}