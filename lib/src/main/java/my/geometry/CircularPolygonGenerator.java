package my.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import my.geometry.quickhullpoly.Point;

/**
 * A random polygon generator that functions by varying the radius and angular
 * spacing of points around a notional circle.
 */
public class CircularPolygonGenerator {

   public static void main(String[] args) {

      Point center = new Point(100, 100);
      double avgRadius = 25;
      double irregularity = 0.5;
      double spikiness = 0.5;

      DrawShape.displayPolygons(() -> {
         int numSides = new Random().nextInt(5, 10);
         List<Point> polygon = new CircularPolygonGenerator().generate(center, avgRadius,
               irregularity, spikiness, numSides);
         return polygon.stream().map(p -> new java.awt.Point((int) p.x, (int) p.y)).toList();
      });
   }

   static class Point {
      double x;
      double y;

      Point(double x, double y) {
         this.x = x;
         this.y = y;
      }
   }

   private final Random random = new Random();

   /**
    * Start with the center of the polygon at center, then creates the polygon by
    * sampling points on a circle around the center. Random noise is added by
    * varying the angular spacing between sequential points, and by varying the
    * radial distance of each point from the centre.
    * 
    * @param center       a pair representing the center of the circumference used
    *                     to generate the polygon.
    * @param avgRadius    the average radius (distance of each generated vertex to
    *                     the center of the circumference) used to generate points
    *                     with a normal distribution.
    * @param irregularity variance of the spacing of the angles between consecutive
    *                     vertices.
    * @param spikiness    variance of the distance of each vertex to the center of
    *                     the circumference.
    * @param numVertices  the number of vertices of the polygon.
    * @return list of vertices, in CCW order.
    */
   public List<Point> generate(Point center, double avgRadius, double irregularity,
         double spikiness, int numVertices) {
      // Parameter check
      if (irregularity < 0 || irregularity > 1) {
         throw new IllegalArgumentException("Irregularity must be between 0 and 1.");
      }
      if (spikiness < 0 || spikiness > 1) {
         throw new IllegalArgumentException("Spikiness must be between 0 and 1.");
      }

      irregularity *= 2 * Math.PI / numVertices;
      spikiness *= avgRadius;
      List<Double> angleSteps = random_angle_steps(numVertices, irregularity);

      // now generate the points
      List<Point> points = new ArrayList<>();
      double angle = random.nextDouble(0, 2 * Math.PI);
      for (int i = 0; i < numVertices; i++) {
         double radius = clip(random.nextGaussian(avgRadius, spikiness), 0, 2 * avgRadius);
         Point point = new Point(center.x + radius * Math.cos(angle),
               center.y + radius * Math.sin(angle));
         points.add(point);
         angle += angleSteps.get(i);
      }
      return points;
   }

   /**
    * Generates the division of a circumference in random angles.
    * 
    * @param steps        the number of angles to generate
    * @param irregularity variance of the spacing of the angles between consecutive
    *                     vertices.
    * @return the list of random angles
    */
   private List<Double> random_angle_steps(int steps, double irregularity) {

      // generate n angle steps
      List<Double> angles = new ArrayList<>();
      double lower = (2 * Math.PI / steps) - irregularity;
      double upper = (2 * Math.PI / steps) + irregularity;
      double cumsum = 0;
      for (int i = 0; i < steps; i++) {
         double angle = random.nextDouble(lower, upper);
         angles.add(angle);
         cumsum += angle;
      }

      // normalize the steps so that point 0 and point n+1 are the same
      cumsum /= (2 * Math.PI);
      for (int i = 0; i < steps; i++) {
         angles.set(i, angles.get(i) / cumsum);
      }
      return angles;
   }

   /**
    * Given an interval, values outside the interval are clipped to the interval
    * edges.
    * 
    * @param value the value to clip
    * @param lower the lower bound
    * @param upper the upper bound
    * @return the nearest value to the value provided in the range from lower to
    *         upper.
    */
   private double clip(double value, double lower, double upper) {
      return Math.min(upper, Math.max(value, lower));
   }
}
