package my.git.mailmap;

import java.util.Objects;

public class Author {
   public static Author parse(String authorString) {
      String[] parts = authorString.split(", ");
      if (parts.length != 2) {
         return null;
      }
      return new Author(parts[0], parts[1]);
   }

   private final String name;

   private final String email;

   public Author(String name, String email) {
      this.name = name;
      this.email = email;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if ((obj == null) || (getClass() != obj.getClass()))
         return false;
      Author other = (Author) obj;
      return Objects.equals(email, other.email) && Objects.equals(name, other.name);
   }

   public String getEmail() {
      return email;
   }

   public String getName() {
      return name;
   }

   @Override
   public int hashCode() {
      return Objects.hash(email, name);
   }

   @Override
   public String toString() {
      return name + ", " + email;
   }

}
