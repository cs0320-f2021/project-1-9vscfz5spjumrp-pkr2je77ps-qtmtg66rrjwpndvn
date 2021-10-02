package edu.brown.cs.student.main.KDTree;

import edu.brown.cs.student.main.ORM.ClassInfoUtil;
import edu.brown.cs.student.main.ORM.FieldInfo;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
   * This method takes in an object and the properties we'd like to compare our Node to. This method
   * returns the summed Euclidian distance over the number of provided indices.
   *
   * @param target          : target Object we are comparing against
   * @param propertyIndices : list of indices for properties we want to index into
   * @return euclidean distance
   */
  @Override
  public double getEuclideanDistance(Object target, List<Integer> propertyIndices)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {

    // get all the field information from the class
    List<FieldInfo> targetFields = ClassInfoUtil.populateFieldInfo(target.getClass());
    List<FieldInfo> nodeFields = ClassInfoUtil.populateFieldInfo(val.getClass());

    double sum = 0;
    for (int index : propertyIndices) {
      double difference = (Integer) targetFields.get(index).readMethod.invoke(target)
          - (Integer) nodeFields.get(index).readMethod.invoke(val);
      sum += Math.pow(difference, 2);
    }
    return sum;
  }

  /**
   * This method returns the straight-line distance between a Node and its target on the relevant
   * axis.
   *
   * @param target        : target Object we are comparing against
   * @param propertyIndex : index for property we want to index into
   * @return axis distance
   */
  @Override
  public double getAxisDistance(Object target, int propertyIndex)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    // get all the field information from the class
    List<FieldInfo> targetFields = ClassInfoUtil.populateFieldInfo(target.getClass());
    List<FieldInfo> nodeFields = ClassInfoUtil.populateFieldInfo(val.getClass());

    return (Integer) targetFields.get(propertyIndex).readMethod.invoke(target)
        - (Integer) nodeFields.get(propertyIndex).readMethod.invoke(val);
  }

  /**
   * This getter method returns the coordinate of a Node, as specified by the property index.
   *
   * @param propertyIndex : index for property we want to index into
   * @return coordinate
   */
  @Override
  public int getCoordinate(int propertyIndex)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    List<FieldInfo> nodeFields = ClassInfoUtil.populateFieldInfo(val.getClass());
    return (Integer) nodeFields.get(propertyIndex).readMethod.invoke(val);
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

