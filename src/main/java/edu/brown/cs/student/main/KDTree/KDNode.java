package edu.brown.cs.student.main.KDTree;

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
  KDNode(Object val, int depth) {
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
   * This method takes in an object and a mapping of. This method
   * returns the summed Euclidean distance over the relevant fields.
   *
   * @param target : target Object we are comparing against
   * @return euclidean distance
   */
  @Override
  public double getEuclideanDistance(Object target)
      throws IllegalAccessException {

    double sum = 0;
    for (Field field : target.getClass().getFields()) {
      double difference = field.getInt(target) - field.getInt(this.getVal());
      sum += Math.pow(difference, 2);
    }
    return sum;
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

    Field field = this.getVal().getClass().getField(fieldName);
    return Math.abs(field.getInt(this.getVal()) - field.getInt(target));
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

