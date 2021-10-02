package edu.brown.cs.student.main.KDTree;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
   * Get the Euclidian distance from a Node to it's target object, using the properties as
   * specified by propertyIndices.
   *
   * @param target          : object that we are comparing against.
   * @param propertyIndices : Node indices we are comparing on.
   * @return euclidean distance
   */
  double getEuclideanDistance(Object target, List<Integer> propertyIndices)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException;

  /**
   * Gets the straight line distance between a Node and it's target on a given axis.
   *
   * @param target        : object that we are comparing against.
   * @param propertyIndex : Node index we are comparing on.
   * @return axis distance
   */
  double getAxisDistance(Object target, int propertyIndex)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException;

  /**
   * Get a Node's coordinate given the propertyIndex we're searching for.
   *
   * @param propertyIndex : which coordinate we want to access.
   * @return coordinate
   */
  int getCoordinate(int propertyIndex)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException;


}
