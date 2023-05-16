package my.geometry;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BresenhamLineAlgorithm {

   /**
    * Draws a line using Bresenham's line algorithm between two points (x0, y0) and
    * (x1, y1).
    * 
    * @param x0 The x-coordinate of the starting point.
    * @param y0 The y-coordinate of the starting point.
    * @param x1 The x-coordinate of the ending point.
    * @param y1 The y-coordinate of the ending point.
    * @return A list of Point objects representing the pixels on the line.
    */
   public static List<Point> drawLine(int x0, int y0, int x1, int y1) {
      List<Point> points = new ArrayList<>();

      int dx = Math.abs(x1 - x0);
      int dy = Math.abs(y1 - y0);

      int sx = x0 < x1 ? 1 : -1; // Step direction along the x-axis
      int sy = y0 < y1 ? 1 : -1; // Step direction along the y-axis

      int error = dx - dy;

      while (true) {
         points.add(new Point(x0, y0)); // Add current point to the list

         if (x0 == x1 && y0 == y1) {
            break; // Exit the loop when the end point is reached
         }

         int e2 = 2 * error;

         if (e2 > -dy) {
            error -= dy;
            x0 += sx; // Move along the x-axis in the specified direction
         }

         if (e2 < dx) {
            error += dx;
            y0 += sy; // Move along the y-axis in the specified direction
         }
      }

      return points;
   }
}
