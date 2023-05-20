package my.geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Draws a shape
 */
public class DrawShape {

   private static final int OFFSET = 10;
   private static final int GROW_FACTOR = 2;

   public static void displayPolygons(Supplier<List<Point>> polygonSupplier) {

      List<Point> points = new ArrayList<>();
      JPanel polygonPanel = new JPanel() {

         private static final long serialVersionUID = -2315842704779601602L;

         @Override
         public void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);

            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;

            for (var p : points) {
               minX = Math.min(minX, p.x);
               minY = Math.min(minY, p.y);
            }

            int j = points.size() - 1;
            for (int i = 0; i < points.size(); i++) {
               g.drawLine((int) ((points.get(j).getX() - minX) * GROW_FACTOR + OFFSET),
                     (int) ((points.get(j).getY() - minY) * GROW_FACTOR + OFFSET),
                     (int) ((points.get(i).getX() - minX) * GROW_FACTOR + OFFSET),
                     (int) ((points.get(i).getY() - minY) * GROW_FACTOR + OFFSET));
               j = i;
            }

         };
      };

      JButton button = new JButton("Next");

      JPanel content = new JPanel();
      GridBagLayout gbl = new GridBagLayout();
      gbl.columnWidths = new int[] { 5, 200, 5 };
      gbl.columnWeights = new double[] { 1.0, 0.0, 1.0 };
      gbl.rowHeights = new int[] { 200, 30 };
      gbl.rowWeights = new double[] { 1.0, 0.0 };

      GridBagConstraints gbcPanel = new GridBagConstraints();
      gbcPanel.gridx = 1;
      gbcPanel.gridy = 0;
      gbcPanel.fill = GridBagConstraints.BOTH;
      gbcPanel.anchor = GridBagConstraints.CENTER;

      gbl.setConstraints(polygonPanel, gbcPanel);

      GridBagConstraints gbcBtn = new GridBagConstraints();
      gbcBtn.gridx = 1;
      gbcBtn.gridy = 1;
      gbl.setConstraints(button, gbcBtn);

      content.setLayout(gbl);
      content.add(polygonPanel);
      content.add(button);

      button.addActionListener((evt) -> {
         List<Point> points2 = polygonSupplier.get();
         points.clear();
         points.addAll(points2);
         content.repaint();
      });

      JFrame frame = new JFrame("Polygon");

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(content);

      frame.pack();
      frame.setVisible(true);

   }

}
