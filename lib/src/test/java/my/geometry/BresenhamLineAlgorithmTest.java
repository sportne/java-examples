package my.geometry;

import java.awt.Point;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BresenhamLineAlgorithmTest {

   @Test
   public void given_downward_line_when_drawLine_then_correct_points_returned() {
      int x0 = 2;
      int y0 = 2;
      int x1 = 2;
      int y1 = 6;

      List<Point> points = BresenhamLineAlgorithm.drawLine(x0, y0, x1, y1);

      Assert.assertEquals(5, points.size());
      Assert.assertEquals(new Point(2, 2), points.get(0));
      Assert.assertEquals(new Point(2, 3), points.get(1));
      Assert.assertEquals(new Point(2, 4), points.get(2));
      Assert.assertEquals(new Point(2, 5), points.get(3));
      Assert.assertEquals(new Point(2, 6), points.get(4));
   }

   @Test
   public void given_upward_line_when_drawLine_then_correct_points_returned() {
      int x0 = 4;
      int y0 = 6;
      int x1 = 4;
      int y1 = 2;

      List<Point> points = BresenhamLineAlgorithm.drawLine(x0, y0, x1, y1);

      Assert.assertEquals(5, points.size());
      Assert.assertEquals(new Point(4, 6), points.get(0));
      Assert.assertEquals(new Point(4, 5), points.get(1));
      Assert.assertEquals(new Point(4, 4), points.get(2));
      Assert.assertEquals(new Point(4, 3), points.get(3));
      Assert.assertEquals(new Point(4, 2), points.get(4));
   }

   @Test
   public void given_rightward_line_when_drawLine_then_correct_points_returned() {
      int x0 = 2;
      int y0 = 4;
      int x1 = 6;
      int y1 = 4;

      List<Point> points = BresenhamLineAlgorithm.drawLine(x0, y0, x1, y1);

      Assert.assertEquals(5, points.size());
      Assert.assertEquals(new Point(2, 4), points.get(0));
      Assert.assertEquals(new Point(3, 4), points.get(1));
      Assert.assertEquals(new Point(4, 4), points.get(2));
      Assert.assertEquals(new Point(5, 4), points.get(3));
      Assert.assertEquals(new Point(6, 4), points.get(4));
   }

   @Test
   public void given_leftward_line_when_drawLine_then_correct_points_returned() {
      int x0 = 6;
      int y0 = 2;
      int x1 = 2;
      int y1 = 2;

      List<Point> points = BresenhamLineAlgorithm.drawLine(x0, y0, x1, y1);

      Assert.assertEquals(5, points.size());
      Assert.assertEquals(new Point(6, 2), points.get(0));
      Assert.assertEquals(new Point(5, 2), points.get(1));
      Assert.assertEquals(new Point(4, 2), points.get(2));
      Assert.assertEquals(new Point(3, 2), points.get(3));
      Assert.assertEquals(new Point(2, 2), points.get(4));
   }

   @Test
   public void given_diagonal_line_when_drawLine_then_correct_points_returned() {
      int x0 = 1;
      int y0 = 1;
      int x1 = 5;
      int y1 = 5;

      List<Point> points = BresenhamLineAlgorithm.drawLine(x0, y0, x1, y1);
      Assert.assertEquals(5, points.size());
      Assert.assertEquals(new Point(1, 1), points.get(0));
      Assert.assertEquals(new Point(2, 2), points.get(1));
      Assert.assertEquals(new Point(3, 3), points.get(2));
      Assert.assertEquals(new Point(4, 4), points.get(3));
      Assert.assertEquals(new Point(5, 5), points.get(4));
   }

   @Test
   public void given_a_single_point_line_when_drawLine_then_correct_points_returned() {
      int x0 = 2;
      int y0 = 3;
      int x1 = 2;
      int y1 = 3;

      List<Point> points = BresenhamLineAlgorithm.drawLine(x0, y0, x1, y1);

      Assert.assertEquals(1, points.size());
      Assert.assertEquals(new Point(2, 3), points.get(0));
   }

}
