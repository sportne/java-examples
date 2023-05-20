package my.geometry.quickhullpoly;

import java.util.Objects;

public class Edge {

   private Point begin;
   private Point end;

   Edge(Point begin, Point end) {
      this.begin = begin;
      this.end = end;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if ((obj == null) || (getClass() != obj.getClass()))
         return false;
      Edge other = (Edge) obj;
      return Objects.equals(begin, other.begin) && Objects.equals(end, other.end);
   }

   public Point getBegin() {
      return begin;
   }

   public Point getEnd() {
      return end;
   }

   @Override
   public int hashCode() {
      return Objects.hash(begin, end);
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Edge [begin=");
      builder.append(begin);
      builder.append(", end=");
      builder.append(end);
      builder.append("]");
      return builder.toString();
   }

   // checks if the two new edges created by the point are valid (i.e. do not
   // intersect the polygon)
   boolean valid(Polygon polygon, Point point) {
      Edge e1 = new Edge(begin, point);
      Edge e2 = new Edge(point, end);
      return !polygon.intersect(e1) && !polygon.intersect(e2);
   }

}
