package my.geometry.quickhullpoly;

import java.util.Objects;

public class Point {

   public double x;
   public double y;

   public Point(double x, double y) {
      this.x = x;
      this.y = y;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if ((obj == null) || (getClass() != obj.getClass()))
         return false;
      Point other = (Point) obj;
      return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
            && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
   }

   @Override
   public int hashCode() {
      return Objects.hash(x, y);
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Point [x=");
      builder.append(x);
      builder.append(", y=");
      builder.append(y);
      builder.append("]");
      return builder.toString();
   }

}
