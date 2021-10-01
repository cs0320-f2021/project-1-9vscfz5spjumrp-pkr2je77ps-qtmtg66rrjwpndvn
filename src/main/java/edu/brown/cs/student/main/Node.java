package edu.brown.cs.student.main;

import java.util.List;

//TODO: write interface for Node and then write class as KDNode

/**
 * Should T just extend a list
 * @param <T>
 */
public final class Node<T extends Comparable<T>> {
  private Node left;
  private Node right;
  private final T val;
  private final int depth;

  /**
   * The Node constructor takes in the object that it wants to hold (val), as
   * well as its depth. Both children are initialized as null.
   *
   * @param val:   item to be stored in the Node
   * @param depth: how deep the Node is in the tree
   */
  Node(T val, int depth) {
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
  public T getVal() {
    return val;
  }

  /**
   * Getter method for Node's left child.
   *
   * @return left
   */
  public Node<T> getLeft() {
    return left;
  }

  /**
   * Setter method to set Node's left child
   *
   * @param leftChild: left child
   */
  public void setLeft(Node leftChild) {
    left = leftChild;
  }

  /**
   * Getter method for Node's right child.
   *
   * @return right
   */
  public Node getRight() {
    return right;
  }

  /**
   * Setter method to set Node's right child
   *
   * @param rightChild: right child
   */
  public void setRight(Node rightChild) {
    left = rightChild;
  }

  /**
   * Getter method for Node's depth.
   *
   * @return depth
   */
  public int getDepth() {
    return depth;
  }

  //TODO: how do we make this generic to any type of thing. This actually needs to be a method of record.
  // Make interface that says it has this function, then we don't have to worry about it.
  public <T extends Comparable<T>> double getEuclidianDistance(Node node, List<Integer> propertyIndices) {
    //TODO: type check things
    double sum = 0;
    for (int index : propertyIndices) {
      double difference = (double) this.getVal().get(index) - (double) node.getVal().get(index);
      sum += Math.pow(difference, 2);
    }
    return sum;
  }
}
