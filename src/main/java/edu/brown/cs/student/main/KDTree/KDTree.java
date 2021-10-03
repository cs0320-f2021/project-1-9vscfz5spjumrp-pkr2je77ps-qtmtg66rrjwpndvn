package edu.brown.cs.student.main.KDTree;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KDTree is a data structure that can be thought of a BST for n different axes to sort on.
 */
final class KDTree {

  private final Node root;
  private final Map<Integer, String> axisToFieldMap;
  private final int numAxes;

  /**
   * the KDTree class stores a KD-Tree starting with a root node. It needs to know how many
   * dimensions it sort on, as well as which Class fields it should sort on.
   *
   * @param vals       : collection of objects
   * @param dimensions : how many axes we want our KD-Tree to sort on
   */
  <T extends Comparable<T>> KDTree(Collection<T> vals, int dimensions, List<String> fields) {
    //Check valid input
    if (vals == null) {
      throw new IllegalArgumentException("The collection of values that you provide to the KDTree"
          + "constructor cannot be null");
    }
    if (dimensions == 0) {
      throw new IllegalArgumentException("You must provide more than 0 dimensions for your"
          + "KDTree to sort on.");
    }

    //Create mapping from axis to field, allowing us to partition objects
    axisToFieldMap = new HashMap<>();
    for (int i = 0; i < dimensions; i++) {
      axisToFieldMap.put(i, fields.get(i));
    }
    numAxes = axisToFieldMap.size();


    //Convert collections of objects to a list of KDSortableByAxis objects, so that we can compare
    //them by axis & field later.
    List<SortableByAxis<T>> values = new ArrayList<>();
    for (T val : vals) {
      SortableByAxis<T> s = new KDSortableByAxis<>(val, axisToFieldMap.get(0), 0);
      values.add(s);
    }

    //Sort the values based on the relevant axis. This should sort based on the relevant axis.
    Collections.sort(values);

    //get the middle element
    int median = vals.size() / 2;
    SortableByAxis<T> val = values.get(median);

    //set the middle element as the root.
    root = new KDNode(val, 0);

    //set the root's children. This calls kDTreeBuilder, which will recursively build the tree.
    root.setLeft(kDTreeBuilder(values.subList(0, median), 1, dimensions));
    root.setRight(kDTreeBuilder(values.subList(median, vals.size()), 1, dimensions));
  }

  /**
   * This is a helper method that recursively builds our KDTree.
   *
   * @param vals:       list of items left to insert
   * @param depth:      how deep we are in the KDTree
   * @param dimensions: how many dimensions we have
   */
  private <T extends Comparable<T>> Node kDTreeBuilder(List<SortableByAxis<T>> vals, int depth,
                                                       int dimensions) {
    //Check valid input
    if (vals == null) {
      throw new IllegalArgumentException("The collection of values that you provide to the KDTree"
          + "constructor cannot be null");
    }
    if (dimensions == 0) {
      throw new IllegalArgumentException("You must provide more than 0 dimensions for your"
          + "KDTree to sort on.");
    }

    //Base case: if there are no Objects left to sort, return null to parent Node.
    if (vals.size() == 0) {
      return null;
    }

    //Reset the axis and fields we're comparing on for the SortableByAxis objects
    for (SortableByAxis<T> val : vals) {
      int axis = depth % dimensions;
      val.setAxis(axis);
      val.setFieldName(axisToFieldMap.get(axis));
    }

    Collections.sort(vals);

    //get the middle element
    int median = vals.size() / 2;
    SortableByAxis<T> val = vals.get(median);

    //create a node out of that middle element
    Node node = new KDNode(val, depth);

    //set children
    node.setLeft(kDTreeBuilder(vals.subList(0, median), depth + 1, dimensions));
    node.setRight(kDTreeBuilder(vals.subList(median, vals.size()), depth + 1, dimensions));
    return node;
  }

  /**
   * This is our main function. It relies on kNN(), which recursively finds the nearest neighbors.
   *
   * @param target          : Object we're comparing against
   * @param k               : number of nearest neighbors.
   * @return k nearest neighbors
   */
  //TODO: need to worry about whether target IS our neighbor (just check ID)
  private List<Object> kNearestNeighbors(Object target, int k)
      throws IllegalAccessException, NoSuchFieldException {
    //Create nearestNeighbors list and get nearest neighbors
    List<Node> nearestNeighbors = new ArrayList<>();
    nearestNeighbors = kNN(root, target, k, nearestNeighbors);

    //Convert nearestNeighbors from list of Nodes to list of Objects
    List<Object> nearestObjects = new ArrayList<>();
    for (Node node : nearestNeighbors) {
      nearestObjects.add(node.getVal());
    }

    //Return k-nearest objects. Pretty sure we have to reverse given the way that my code works.
    Collections.reverse(nearestObjects);
    return nearestObjects;
  }

  /**
   * kNN takes in an Object and finds the k nearest neighbors to that Object based
   * on a list of given properties. SEE HANDOUT: I copied pseudocode from there.
   * Doc: https://docs.google.com/document/d/1TYUbEgyh7oQEt0nW9pmGO-_PEmnkuGqBfuItbuqFk6s/edit
   *
   * @param target          : Object we're comparing against
   * @param k               : number of nearest neighbors.
   * @return list of k-nearest Nodes.
   */
  private List<Node> kNN(Node node, Object target, int k, List<Node> nearestNeighbors)
      throws IllegalAccessException, NoSuchFieldException {
    //Base case: null Node.
    if (node == null) {
      return nearestNeighbors;
    }

    //    Get the straight-line (Euclidean) distance from your target point to the current node.
    double distance = node.getEuclideanDistance(target);

//    If the current node is closer to your target point than one of your k-nearest neighbors,
//    or if your collection of neighbors is not full, update the list accordingly
    int size = nearestNeighbors.size();

    if (size == 0) {
      nearestNeighbors.add(node);
    } else if (size < k) {
      int pos = getNodePosition(target, nearestNeighbors, node);
      nearestNeighbors.add(pos, node);
    } else {
      Node furthestNeighbor = nearestNeighbors.get(0);
      if (distance
          < furthestNeighbor.getEuclideanDistance(target)
      ) {
        nearestNeighbors.remove(0);
        int pos = getNodePosition(target, nearestNeighbors, node);
        nearestNeighbors.add(pos, node);
      }
    }

    // find relevant axis by depth and get associated field name
    int axis = node.getDepth() % numAxes;
    String fieldName = axisToFieldMap.get(axis);

//    If the Euclidean distance between the target point and the farthest
//    “best neighbor” you have so far is greater than the relevant axis distance* between
//    the current node and target point, recur on both children.
    Node furthestNeighbor = nearestNeighbors.get(0);
    double furthestNeighborDistance = furthestNeighbor.getEuclideanDistance(target);
    double relevantAxisDistance = node.getAxisDistance(target, fieldName);

    //recur on both children. Second part of OR operator is found at "**Note:" in handout
    if (furthestNeighborDistance > relevantAxisDistance || size < k) {
      nearestNeighbors = kNN(node.getRight(), target, k, nearestNeighbors);
      nearestNeighbors = kNN(node.getLeft(), target, k, nearestNeighbors);
    } else {
//      If the current node's coordinate on the relevant axis is less than the target's coordinate
//      on the relevant axis, recur on the right child.
      if (node.getCoordinate(fieldName) < this.getTargetCoordinate(target, fieldName)) {
        nearestNeighbors = kNN(node.getRight(), target, k, nearestNeighbors);
      } else {
//      Else if the current node's coordinate on the relevant axis is greater than the target's
//      coordinate on the relevant axis, recur on the left child.
        nearestNeighbors = kNN(node.getLeft(), target, k, nearestNeighbors);
      }
    }
    return nearestNeighbors;
  }

  /**
   * Helper method invoked in kNN() that gets the target coordinate for a specific property.
   *
   * @param target: Object we're comparing against
   * @param fieldName:  field used to access coordinate
   * @return coordinate
   */
  private int getTargetCoordinate(Object target, String fieldName)
      throws IllegalAccessException, NoSuchFieldException {

    Field field = target.getClass().getField(fieldName);
    return field.getInt(target);
  }

  /**
   * This helper method returns the position of a Node in a list. It is used in kNN()
   * to update the Queue when it has not yet reached k, where k is the number of nearest neighbors
   * that we want to return.
   *
   * @param nearestNeighbors: list of nearest neighbors.
   * @param node:             node that we want to find the position for.
   * @return position in list
   */
  private int getNodePosition(Object target, List<Node> nearestNeighbors, Node node)
      throws IllegalAccessException {
    for (int i = 0; i < nearestNeighbors.size(); i++) {
      Node furthestNode = nearestNeighbors.get(i);
      if (furthestNode.getEuclideanDistance(target)
          < node.getEuclideanDistance(target)) {
        return i;
      }
    }
    return nearestNeighbors.size();
  }

  /**
   * classify takes in an object, and uses the k nearest neighbors
   * to determine what property that object would likely have.
   *
   * @param target        : target object we are comparing against
   * @param k             : number of nearest neighbors we will use to classify.
   * @param propertyIndex : index for property we care about
   * @return classification
   */
  private Object classify(Object target, int k, int propertyIndex)
      throws IllegalAccessException, NoSuchFieldException {
    List<Integer> properties = new ArrayList<>(List.of(propertyIndex));
    List<Object> nearestNeighbors = kNearestNeighbors(target, k);
    return null;
  }

}
















