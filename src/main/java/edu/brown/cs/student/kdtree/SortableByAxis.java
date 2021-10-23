package edu.brown.cs.student.kdtree;

import org.jetbrains.annotations.NotNull;

/**
 * The SortableByAxis interface allows us to wrap objects and sort them by their axis and
 * corresponding field.
 *
 * @param <T>
 */
interface SortableByAxis<T extends Comparable<T>> extends Comparable<SortableByAxis<T>> {

  /**
   * Gets the axis.
   *
   * @return axis
   */
  int getAxis();

  /**
   * Sets the axis.
   */
  void setAxis(int newAxis);

  /**
   * Gets the field name.
   *
   * @return field name
   */
  String getFieldName();

  /**
   * Sets the field name.
   */
  void setFieldName(String newFieldName);

  /**
   * Gets the underlying Object.
   *
   * @return object
   */
  Object getVal();

  /**
   * compares two SortableByAxis implementations.
   *
   * @param o: other SortableByAxis implementation we are comparing against
   * @return comparison
   */
  @Override
  int compareTo(@NotNull SortableByAxis<T> o);
}
