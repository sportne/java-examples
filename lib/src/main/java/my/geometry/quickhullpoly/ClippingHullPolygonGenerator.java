package my.geometry.quickhullpoly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.apache.commons.math3.util.Pair;

import my.geometry.DrawShape;

/**
 * A random polygon generator that functions by removing edges from a convex
 * hull.
 */
public class ClippingHullPolygonGenerator {

   public static void main(String[] args) {
      DrawShape.displayPolygons(() -> {
         int numSides = new Random().nextInt(5, 10);
         List<Point> polygon = generate(10, 100, 10, 100, numSides);
         return polygon.stream().map(p -> new java.awt.Point((int) p.x, (int) p.y)).toList();
      });
   }

   private static List<Point> removePointsFromList(List<Point> points, List<Point> toRemove) {
      List<Point> reducedPoints = new ArrayList<>();
      for (var p : points) {
         if (!toRemove.contains(p)) {
            reducedPoints.add(p);
         }
      }

      return reducedPoints;
   }

   private static Map<Double, Pair<Edge, Point>> precomputePointToEdgeDistances(Polygon polygon,
         List<Point> points) {
      Map<Double, Pair<Edge, Point>> distancesDict = new HashMap<>();

      for (Edge edge : polygon.getEdges()) {
         for (Point point : points) {
            double currDist = PointUtils.pointToLine(point, edge.getBegin(), edge.getEnd());
            distancesDict.put(currDist, Pair.create(edge, point));
         }
      }

      return distancesDict;
   }

   private static List<Point> randomPoints(double minX, double maxX, double minY, double maxY,
         int numVertices) {
      List<Point> points = new ArrayList<>(numVertices);
      Random random = new Random();
      for (int i = 0; i < numVertices; i++) {
         points.add(new Point((maxX - minX) * random.nextDouble() + minX,
               (maxY - minY) * random.nextDouble() + minY));
      }
      return points;
   }

   private static Polygon quickHullPolygon(List<Point> points)
   {
      List<Point> quickHullPoints = Quickhull.quickhull(points);
      List<Edge> edges = new ArrayList<>();
      int j = quickHullPoints.size() - 1;
      for (int i = 0; i < quickHullPoints.size(); i++) {
         edges.add(new Edge(quickHullPoints.get(j), quickHullPoints.get(i)));
         j = i;
      }

      return new Polygon(quickHullPoints, edges);
   }
   
   private static boolean containsEdge(Polygon polygon, Edge e)
   {
      for (var edge : polygon.getEdges()) {
         if (e.getBegin() == edge.getBegin() && e.getEnd() == edge.getEnd()) {
            return true;
         }
      }
      return false;
   }
   
   public static List<Point> generate(double minX, double maxX, double minY, double maxY,
         int numVertices) {

      // collect random points up to the desired number
      List<Point> points = randomPoints(minX, maxX, minY, maxY, numVertices);

      // initialize the polygon as a convex hull of the points
      Polygon polygon = quickHullPolygon(points);

      // update points set to exclude polygon vertices
      List<Point> interiorPoints = removePointsFromList(points, polygon.getVertices());

      // pre-process by adding the distances between every edge-point pair into MinPQ
      // and dictionary
      Map<Double, Pair<Edge, Point>> distancesDict = precomputePointToEdgeDistances(polygon,
            interiorPoints);
      PriorityQueue<Double> distances = new PriorityQueue<>();
      for (var key : distancesDict.keySet()) {
         distances.add(key);
      }

      // iterate so long as there still exist points in the interior
      int i = interiorPoints.size();
      while (!interiorPoints.isEmpty() && !distances.isEmpty()) {

         // get the current shortest distance and its corresponding edge-point pair
         double currDist = distances.poll();

         Pair<Edge, Point> pair = distancesDict.get(currDist);
         Edge e = pair.getFirst();
         Point point = pair.getSecond();

         // check if the edge exists in the current polygon
         boolean containsEdge = containsEdge(polygon, e);

         // proceed only if edge is in polygon, point is in interior, and it is a
         // valid addition to the polygon
         if (containsEdge && interiorPoints.contains(point) && e.valid(polygon, point)) {
            // get index of the edge
            for (var edge : polygon.getEdges()) {
               if (e.getBegin() == edge.getBegin() && e.getEnd() == edge.getEnd()) {
                  i = polygon.getEdges().indexOf(edge);
                  break;
               }
            }
            // insert new edges and point into the polygon,
            // remove old edge from polygon and remove point from interior
            Edge e1 = new Edge(e.getBegin(), point);
            Edge e2 = new Edge(point, e.getEnd());
            
            polygon.getVertices().add(i, point);
            polygon.getEdges().set(i, e1);
            polygon.getEdges().add(i + 1, e2);
            interiorPoints.remove(point);
            // update priority queue by adding distances between two new edges
            // and every point in interior
            for (int k = 0; k < interiorPoints.size(); k++) {
               point = interiorPoints.get(k);
               double currDistE1 = PointUtils.pointToLine(point, e1.getBegin(), e1.getEnd());
               double currDistE2 = PointUtils.pointToLine(point, e2.getBegin(), e2.getEnd());
               distances.add(currDistE1);
               distances.add(currDistE2);
               distancesDict.put(currDistE1, Pair.create(e1, point));
               distancesDict.put(currDistE2, Pair.create(e2, point));
            }
         }
      }

      return polygon.getVertices();
   }

}
