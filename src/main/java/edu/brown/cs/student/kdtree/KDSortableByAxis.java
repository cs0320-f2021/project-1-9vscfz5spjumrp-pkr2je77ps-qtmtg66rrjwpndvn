package edu.brown.cs.student.kdtree;

import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;

/**
 * The KDSortableByAxis class implements the SortableByAxis interface, which wraps an Object
 * and allows us to sort them by their axis. See KDTree/SortableByAxis interface requirements.
 *
 * @param <T>
 */
public class KDSortableByAxis<T extends Comparable<T>> implements SortableByAxis<T> {
  private final Object val;
  private int axis;
  private String fieldName;

  /**
   * Constructor passes in the underlying object and sets the axis to 0. The axis is updated
   * as the KDTree recursively builds itself.
   *
   * @param object : underlying Object
   * @param field: field we'll compare on
   * @param axis: axis we'll compare on
   */
  public KDSortableByAxis(Object object, String field, int axis) {
    this.val = object;
    this.axis = axis;
    this.fieldName = field;
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
  public Object getVal() {
    return val;
  }

  /**
   * Gets the field. This method is used when comparing two SortableByAxis<T> implementations
   * on a given axis. See compareTo() as defined below.
   *
   * @return axis
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Sets the field. This method is called in KDTree's kNN(), which recursively builds the tree.
   * At each split, kNN() will update the KDSortableByAxis's field, so that we can compare it to
   * other KDSortableByAxis objects later.
   *
   * @param newFieldName : the new axis we are setting for this SortableByAxis implementation
   */
  public void setFieldName(String newFieldName) {
    fieldName = newFieldName;
  }

  /**
   * Compares two SortableByAxis implementations on their fields.
   *
   * @param o: other SortableByAxis implementation we are comparing against
   * @return comparison
   */
  @Override
  public int compareTo(@NotNull SortableByAxis<T> o) {
    String name = this.getFieldName();
    Field field = null;
    try {
      field = this.getVal().getClass().getField(name);
    } catch (NoSuchFieldException e) {
      System.out.println("KDSortableByAxis Error: field to compare on was not found");
    }
    try {
      assert field != null;
      return Integer.compare(field.getInt(this.getVal()), field.getInt(o.getVal()));
    } catch (IllegalAccessException e) {
      System.out.println("KDSortableByAxis Error: could not access desired field");
    }
    return 0;
  }
}
