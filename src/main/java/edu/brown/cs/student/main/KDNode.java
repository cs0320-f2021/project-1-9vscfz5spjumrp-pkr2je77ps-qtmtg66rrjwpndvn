package edu.brown.cs.student.main;

import java.util.List;

//TODO: write interface for Node and then write class as KDNode

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
   * Getter method for Node's right child.
   *
   * @return right
   */
  @Override
  public Node getRight() {
    return right;
  }

  /**
   * Setter method to set Node's right child
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

//  //TODO: how do we make this generic to any type of thing. This actually needs to be a method of record.
//  // Make interface that says it has this function, then we don't have to worry about it.
//  public <T extends Comparable<T>> double getEuclidianDistance(Node node, List<Integer> propertyIndices) {
//    //TODO: type check things
//    double sum = 0;
//    for (int index : propertyIndices) {
//      double difference = (double) this.getVal().get(index) - (double) node.getVal().get(index);
//      sum += Math.pow(difference, 2);
//    }
//    return sum;
//  }
}
