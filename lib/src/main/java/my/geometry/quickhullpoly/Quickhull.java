package my.geometry.quickhullpoly;

import java.util.ArrayList;
import java.util.List;

public class Quickhull {

   private static List<Point> quickhullRec(Point a, Point b, List<Point> points) {
      List<Point> convexHull = new ArrayList<>();

      // find furthest point in points from AB
      Point furthest = null;
      double maxdist = 0;
      List<Point> newPoints = new ArrayList<>();
      for (var point : points) {
         if (PointUtils.signedArea(a, b, point) > 0) {
            newPoints.add(point);
            if (PointUtils.signedArea(a, b, point) > maxdist) {
               maxdist = PointUtils.signedArea(a, b, point);
               furthest = point;
            }
         }
      }
      if (maxdist == 0) {
         convexHull.add(new Point(a.x, a.y));
         return convexHull;
      }
      convexHull.addAll(quickhullRec(a, furthest, newPoints));
      convexHull.addAll(quickhullRec(furthest, b, newPoints));

      return convexHull;
   }

   static List<Point> quickhull(List<Point> points) {
      // initialize convex hull and points list
      List<Point> convexHull = new ArrayList<>();
      double minval = Double.MAX_VALUE;
      double maxval = Double.MIN_VALUE;
      double yMin = -1;
      double yMax = -1;

      for (var point : points) {
         if (point.x > maxval) {
            maxval = point.x;
            yMax = point.y;
         }
         if (point.x < minval) {
            minval = point.x;
            yMin = point.y;
         }
      }

      // find horizontal max and min points
      List<Point> set1 = new ArrayList<>();
      List<Point> set2 = new ArrayList<>();
      for (var point : points) {
         double ccwVal = PointUtils.signedArea(new Point(minval, yMin), new Point(maxval, yMax), point);
         if (ccwVal > 0) {
            set1.add(point);
         } else if (ccwVal < 0) {
            set2.add(point);
         }
      }

      convexHull.addAll(quickhullRec(new Point(minval, yMin), new Point(maxval, yMax), set1));
      convexHull.addAll(quickhullRec(new Point(maxval, yMax), new Point(minval, yMin), set2));

      return convexHull;
   }
}
