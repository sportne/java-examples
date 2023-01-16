package my.git.mailmap.gui;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

public class SortedListModelTest {

   private SortedListModel<Integer> model;

   @Before
   public void setup() {
      model = new SortedListModel<Integer>(Comparator.naturalOrder());
   }

   @Test
   public void testAddElement() {
      model.addElement(3);
      model.addElement(1);
      model.addElement(2);
      assertEquals(3, model.size());
      assertEquals(Integer.valueOf(1), model.getElementAt(0));
      assertEquals(Integer.valueOf(2), model.getElementAt(1));
      assertEquals(Integer.valueOf(3), model.getElementAt(2));
   }

   @Test
   public void testClear() {
      model.addElement(3);
      model.addElement(1);
      model.addElement(2);
      model.clear();
      assertEquals(0, model.size());
   }

   @Test
   public void testRemoveElement() {
      model.addElement(3);
      model.addElement(1);
      model.addElement(2);
      model.removeElement(1);
      assertEquals(2, model.size());
      assertEquals(Integer.valueOf(2), model.getElementAt(0));
      assertEquals(Integer.valueOf(3), model.getElementAt(1));
   }

   @Test
   public void testSet() {
      model.addElement(3);
      model.addElement(1);
      model.addElement(2);
      model.set(1, 4);
      assertEquals(3, model.size());
      assertEquals(Integer.valueOf(1), model.getElementAt(0));
      assertEquals(Integer.valueOf(3), model.getElementAt(1));
      assertEquals(Integer.valueOf(4), model.getElementAt(2));
   }
}
