package edu.brown.cs.student.kdtree;

import java.lang.reflect.Field;

public final class KDNode implements Node {
  private Node left;
  private Node right;
  private final Object val;
  private final int depth;

  /**
   * The Node constructor takes in the object that it wants to hold (val), as
   * well as its depth. Both children are initialized as null.
   *
   * @param val:   item to be stored in the Node
   * @param depth: how deep the Node is in the tree
   */
  public KDNode(Object val, int depth) {
    left = null;
    right = null;
    this.val = val;
    this.depth = depth;
  }

  /**
   * Getter method for Node's val.
   *
   * @return val
   */
  @Override
  public Object getVal() {
    return val;
  }

  /**
   * Getter method for Node's left child.
   *
   * @return left
   */
  @Override
  public Node getLeft() {
    return left;
  }

  /**
   * Setter method to set Node's left child
   *
   * @param leftChild: left child
   */
  @Override
  public void setLeft(Node leftChild) {
    left = leftChild;
  }

  /**
   * This method takes in an object and returns the summed Euclidean distance over the relevant
   * fields.
   *
   * @param target : target Object we are comparing against
   * @return euclidean distance
   */
  @Override
  public double getEuclideanDistance(Object target)
      throws IllegalAccessException, NoSuchFieldException {

    double sum = 0;
    for (Field field : target.getClass().getFields()) {
      String fieldName = field.getName();
      Field valField = this.getVal().getClass().getField(fieldName);
      double difference = field.getInt(target) - valField.getInt(this.getVal());
      sum += Math.pow(difference, 2);
    }
    return Math.sqrt(sum);
  }

  /**
   * This method returns the straight-line distance between a Node and its target on the relevant
   * axis.
   *
   * @param target : target Object we are comparing against
   * @return axis distance
   */
  @Override
  public double getAxisDistance(Object target, String fieldName)
      throws IllegalAccessException,
      NoSuchFieldException {

    Field valField = this.getVal().getClass().getField(fieldName);
    Field targetField = target.getClass().getField(fieldName);
    return Math.abs(valField.getInt(this.getVal()) - targetField.getInt(target));
  }

  /**
   * This getter method returns the coordinate of a Node, as specified by the property index.
   *
   * @return coordinate
   */
  @Override
  public int getCoordinate(String fieldName)
      throws IllegalAccessException, NoSuchFieldException {

    Field field = this.getVal().getClass().getField(fieldName);
    return field.getInt(this.getVal());
  }

  /**
   * Getter method for Node's right child.
   *
   * @return right
   */
  @Override
  public Node getRight() {
    return right;
  }

  /**
   * Setter method to set Node's right child.
   *
   * @param rightChild: right child
   */
  @Override
  public void setRight(Node rightChild) {
    right = rightChild;
  }

  /**
   * Getter method for Node's depth.
   *
   * @return depth
   */
  @Override
  public int getDepth() {
    return depth;
  }
}

