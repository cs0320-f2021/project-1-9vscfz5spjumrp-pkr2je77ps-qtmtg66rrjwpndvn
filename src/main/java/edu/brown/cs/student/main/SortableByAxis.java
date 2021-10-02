package edu.brown.cs.student.main;

public class SortableByAxis<T extends Comparable<T>>
    implements Comparable<SortableByAxis<T>> {
  private Object val;
  private int axis;

  public SortableByAxis(Object object) {
    this.val = object;
    axis = 0;
  }

  public int getAxis() {
    return axis;
  }

  public void setAxis(int newAxis) {
    axis = newAxis;
  }

  public Object getObject() {
    return this.val;
  }

  @Override
  public int compareTo(SortableByAxis<T> o) {
    if (o == null) {
      return 1;
    }
    return 0;
  }
}
