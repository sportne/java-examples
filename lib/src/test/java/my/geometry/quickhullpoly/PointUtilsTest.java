package my.geometry.quickhullpoly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PointUtilsTest {

   @Test
   public void testIntersect_NonIntersectingSegments2_ExpectFalse() {
      // Arrange
      Point a = new Point(1, 1);
      Point b = new Point(5, 5);
      Point c = new Point(5, 1);
      Point d = new Point(2.6, 2.4);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertFalse(result);
   }
   
   @Test
   public void testIntersect_NonIntersectingSegments_ExpectFalse() {
      // Arrange
      Point a = new Point(20.437037922939197, 79.348229207126);
      Point b = new Point(72.54531816422427, 82.47049943263423);
      Point c = new Point(10.961596988400595, 24.493899103453202);
      Point d = new Point(10.45226583932073, 57.082164020949975);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testIntersect_WhenCollinearSegments_ExpectFalse() {
      // Arrange
      Point a = new Point(1, 1);
      Point b = new Point(5, 5);
      Point c = new Point(6, 6);
      Point d = new Point(10, 10);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testIntersect_WhenIntersectingAtEndpoint_ExpectTrue() {
      // Arrange
      Point a = new Point(1, 1);
      Point b = new Point(5, 5);
      Point c = new Point(5, 5);
      Point d = new Point(3, 3);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testIntersect_WhenIntersectingSegments_ExpectTrue() {
      // Arrange
      Point a = new Point(1, 1);
      Point b = new Point(5, 5);
      Point c = new Point(3, 1);
      Point d = new Point(3, 5);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertTrue(result);
   }
   
   @Test
   public void testIntersect_WhenIntersectingSegments2_ExpectTrue() {
      // Arrange
      Point a = new Point(1, 1);
      Point b = new Point(5, 5);
      Point c = new Point(5, 1);
      Point d = new Point(1, 5);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testIntersect_WhenOverlappingSegments_ExpectTrue() {
      // Arrange
      Point a = new Point(1, 1);
      Point b = new Point(5, 5);
      Point c = new Point(2, 2);
      Point d = new Point(4, 4);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertTrue(result);
   }

   @Test
   public void testIntersect_WhenParallelSegments_ExpectFalse() {
      // Arrange
      Point a = new Point(1, 1);
      Point b = new Point(5, 5);
      Point c = new Point(1, 2);
      Point d = new Point(5, 6);

      // Act
      boolean result = PointUtils.intersect(a, b, c, d);

      // Assert
      assertFalse(result);
   }

   @Test
   public void testPointToLine_WhenPointBehindStartEndpoint_ExpectDistanceToStartEndpoint() {
      // Arrange
      Point pnt = new Point(0, 0);
      Point start = new Point(1, 1);
      Point end = new Point(5, 5);

      // Act
      double result = PointUtils.pointToLine(pnt, start, end);

      // Assert
      assertEquals(1.4142, result, 0.0001);
   }

   @Test
   public void testPointToLine_WhenPointBeyondEndEndpoint_ExpectDistanceToEndEndpoint() {
      // Arrange
      Point pnt = new Point(6, 6);
      Point start = new Point(1, 1);
      Point end = new Point(5, 5);

      // Act
      double result = PointUtils.pointToLine(pnt, start, end);

      // Assert
      assertEquals(1.4142, result, 0.0001);
   }

   @Test
   public void testPointToLine_WhenPointOnLine_ExpectZeroDistance() {
      // Arrange
      Point pnt = new Point(3, 3);
      Point start = new Point(1, 1);
      Point end = new Point(5, 5);

      // Act
      double result = PointUtils.pointToLine(pnt, start, end);

      // Assert
      assertEquals(0.0, result, 0.0001);
   }

   @Test
   public void testPointToLine_WhenPointOutsideLineSegment_ExpectDistanceToNearestEndpoint() {
      // Arrange
      Point pnt = new Point(6, 3);
      Point start = new Point(1, 1);
      Point end = new Point(5, 5);

      // Act
      double result = PointUtils.pointToLine(pnt, start, end);

      // Assert
      assertEquals(2.1213, result, 0.0001);
   }

   @Test
   public void testPointToLine_WhenPointPerpendicularToLine_ExpectDistanceEqualToMagnitude() {
      // Arrange
      Point pnt = new Point(3, 5);
      Point start = new Point(1, 1);
      Point end = new Point(5, 1);

      // Act
      double result = PointUtils.pointToLine(pnt, start, end);

      // Assert
      assertEquals(4.0, result, 0.0001);
   }

}
