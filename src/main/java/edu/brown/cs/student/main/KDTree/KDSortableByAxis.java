package edu.brown.cs.student.main.KDTree;

import org.jetbrains.annotations.NotNull;

/**
 * The KDSortableByAxis class implements the SortableByAxis interface, which wraps an Object
 * and allows us to sort them by their axis. See KDTree/SortableByAxis interface requirements.
 *
 * @param <T>
 */
public class KDSortableByAxis<T extends Comparable<T>> implements SortableByAxis<T> {
  private final Object val;
  private int axis;

  /**
   * Constructor passes in the underlying object and sets the axis to 0. The axis is updated
   * as the KDTree recursively builds itself.
   *
   * @param object : underlying Object
   */
  public KDSortableByAxis(Object object) {
    this.val = object;
    axis = 0;
  }

  /**
   * Gets the axis. This method is used when comparing two SortableByAxis<T> implementations
   * on a given axis. See compareTo() as defined below.
   *
   * @return axis
   */
  public int getAxis() {
    return axis;
  }

  /**
   * Sets the axis. This method is called in KDTree's kNN(), which recursively builds the tree.
   * At each split, kNN() will update the KDSortableByAxis's axis, so that we can compare it to
   * other KDSortableByAxis objects later.
   *
   * @param newAxis : the new axis we are setting for this SortableByAxis implementation
   */
  public void setAxis(int newAxis) {
    axis = newAxis;
  }

  /**
   * getObject() is used when comparing two SortableByAxis implementations. We will use the index
   * as defined by the axis to figure out which object we sort on.
   *
   * @return underlying object
   */
  public final Object getVal() {
    return val;
  }

  /**
   * Compares two SortableByAxis implementations on their axis.
   *
   * @param o: other SortableByAxis implementation we are comparing against
   * @return comparison
   */
  @Override
  public int compareTo(@NotNull SortableByAxis<T> o) {
    return 0;
  }
}
