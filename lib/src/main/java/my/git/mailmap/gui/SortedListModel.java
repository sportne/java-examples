package my.git.mailmap.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

public class SortedListModel<E> extends AbstractListModel<E> {

   private static final long serialVersionUID = -134015832887007403L;

   private final List<E> collection;
   private final Comparator<E> comparator;

   public SortedListModel(Comparator<E> comparator) {
      collection = new ArrayList<>();
      this.comparator = comparator;
   }

   public void addElement(E e) {
      collection.add(e);
      collection.sort(comparator);

      fireContentsChanged(this, 0, size() - 1);
   }

   public void clear() {
      collection.clear();
      fireContentsChanged(this, 0, 0);
   }

   public E get(int index) {
      return collection.get(index);
   }

   @Override
   public E getElementAt(int index) {
      return collection.get(index);
   }

   @Override
   public int getSize() {
      return collection.size();
   }

   public void removeElement(E e) {
      collection.remove(e);
      fireContentsChanged(this, 0, size() - 1);
   }

   public void set(int index, E e) {
      collection.set(index, e);
      collection.sort(comparator);
      fireContentsChanged(this, 0, size() - 1);
   }

   public int size() {
      return getSize();
   }

}
