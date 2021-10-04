package edu.brown.cs.student.main.KDTree;

/**
 * The Node interface provides the API that allows us to create tree-like structures.
 */
interface Node {

  /**
   * Get Node's left child.
   *
   * @return Node
   */
  Node getLeft();

  /**
   * Get Node's right child.
   *
   * @return Node
   */
  Node getRight();

  /**
   * Get Node's underlying data, stored in an Object.
   *
   * @return Object
   */
  Object getVal();

  /**
   * Get depth of a Node.
   *
   * @return depth
   */
  int getDepth();

  /**
   * Set Node's right child.
   */
  void setRight(Node rightChild);

  /**
   * Set Node's left child.
   */
  void setLeft(Node leftChild);

  /**
   * Get the Euclidean distance from a Node to its target object.
   *
   * @param target : object that we are comparing against.
   * @return euclidean distance
   */
  double getEuclideanDistance(Object target) throws IllegalAccessException, NoSuchFieldException;

  /**
   * Gets the straight line distance between a Node and its target on a given axis.
   *
   * @param target     : object that we are comparing against.
   * @param fieldName: name of field we want to access
   * @return axis distance
   */
  double getAxisDistance(Object target, String fieldName)
      throws IllegalAccessException, NoSuchFieldException;

  /**
   * Get a Node's coordinate given a field.
   *
   * @param fieldName: name of field we want to access
   * @return coordinate
   */
  int getCoordinate(String fieldName)
      throws IllegalAccessException, NoSuchFieldException;


}
