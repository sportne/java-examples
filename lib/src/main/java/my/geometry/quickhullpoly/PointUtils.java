package my.geometry.quickhullpoly;

public class PointUtils {

   // detect if counterclockwise
   static double signedArea(Point a, Point b, Point c) {
      return (c.y - a.y) * (b.x - a.x) - (b.y - a.y) * (c.x - a.x);
   }

   private static double distance(Point p0, Point p1) {
      return length(vector(p0, p1));
   }

   private static double dot(Point v, Point w) {
      return v.x * w.x + v.y * w.y;
   }

   static boolean intersect(Point a, Point b, Point c, Point d) {
      return doIntersect(new Edge(a, b), new Edge(c, d));
   }

   public static boolean doIntersect(Edge ab, Edge cd) {
      Point a = ab.getBegin();
      Point b = ab.getEnd();
      Point c = cd.getBegin();
      Point d = cd.getEnd();

      double crossProduct = (d.x - c.x) * (b.y - a.y) - (d.y - c.y) * (b.x - a.x);

      if (crossProduct == 0) {
         // Line segments are collinear or parallel
         if (isPointOnLine(a, b, c) || isPointOnLine(a, b, d) || isPointOnLine(c, d, a)
               || isPointOnLine(c, d, b)) {
            return true; // Line segments overlap
         } else {
            return false; // No intersection
         }
      }

      double r = ((c.y - a.y) * (b.x - a.x) - (c.x - a.x) * (b.y - a.y)) / crossProduct;
      double s = ((c.y - a.y) * (d.x - c.x) - (c.x - a.x) * (d.y - c.y)) / crossProduct;

      if (r >= 0 && r <= 1 && s >= 0 && s <= 1) {
         // Intersection point lies within the range of both line segments
         return true;
      }

      return false;
   }

   public static boolean isPointOnLine(Point a, Point b, Point p) {
      double crossProduct = (p.y - a.y) * (b.x - a.x) - (p.x - a.x) * (b.y - a.y);
      if (crossProduct != 0)
         return false;
      double dotProduct = (p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y);
      if (dotProduct < 0)
         return false;
      double squaredLengthBA = (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);
      if (dotProduct > squaredLengthBA)
         return false;
      return true;
   }

   private static double length(Point v) {
      return Math.sqrt(v.x * v.x + v.y * v.y);
   }

   static double pointToLine(Point point, Point start, Point end) {
      Point lineVec = vector(start, end);
      Point pntVec = vector(start, point);
      double lineLen = length(lineVec);
      Point lineUnitvec = unit(lineVec);
      Point pntVecScaled = scale(pntVec, 1.0 / lineLen);
      double t = dot(lineUnitvec, pntVecScaled);
      if (t < 0.0) {
         t = 0.0;
      } else if (t > 1.0) {
         t = 1.0;
      }
      Point nearest = scale(lineVec, t);
      double dist = distance(nearest, pntVec);
      return dist;
   }

   private static Point scale(Point v, double sc) {
      return new Point(v.x * sc, v.y * sc);
   }

   private static Point unit(Point v) {
      double mag = length(v);
      return new Point(v.x / mag, v.y / mag);
   }

   private static Point vector(Point b, Point e) {
      return new Point(e.x - b.x, e.y - b.y);
   }
}
