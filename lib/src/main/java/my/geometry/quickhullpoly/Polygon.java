package my.geometry.quickhullpoly;

import java.util.List;

import org.apache.commons.math3.util.Pair;

public class Polygon {

   private List<Point> vertices;
   private List<Edge> edges;

   Polygon(List<Point> vertices, List<Edge> edges) {
      this.vertices = vertices;
      this.edges = edges;
   }

   // find closest point from pointlist and return the point and the removing edge
   Pair<Edge, Point> findClosest(List<Point> pointlist) {
      double min_dist = Double.MAX_VALUE;
      Edge remEdge = null;
      Point closestPoint = null;
      for (Edge e : edges) {
         for (var point : pointlist) {
            // find point to line distance between the point and the removing
            // edge and potentially update nearest point
            double currDist = PointUtils.pointToLine(point, e.getBegin(), e.getEnd());
            // if the two new edges created by the point are valid
            if (currDist < min_dist) {
               if (e.valid(this, point)) {
                  min_dist = currDist;
                  remEdge = e;
                  closestPoint = point;
               }
            }
         }
      }
      return Pair.create(remEdge, closestPoint);
   }

   public List<Edge> getEdges() {
      return edges;
   }

   public List<Point> getVertices() {
      return vertices;
   }

   // checks whether polygon intersects with a query edge
   boolean intersect(Edge queryEdge) {
      for (var edge : edges) {
         // return true if the query edge and polygon edge don't share a vertex and it
         // intersects with the polygon
         if (edge.getBegin() != queryEdge.getBegin() && edge.getBegin() != queryEdge.getEnd()
               && edge.getEnd() != queryEdge.getBegin() && edge.getEnd() != queryEdge.getEnd()
               && PointUtils.intersect(edge.getBegin(), edge.getEnd(), queryEdge.getBegin(),
                     queryEdge.getEnd())) {
            return true;
         }
      }
      return false;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Polygon [vertices=");
      builder.append(vertices);
      builder.append(", edges=");
      builder.append(edges);
      builder.append("]");
      return builder.toString();
   }

}
