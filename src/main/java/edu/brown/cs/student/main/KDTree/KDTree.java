package edu.brown.cs.student.main.KDTree;

import edu.brown.cs.student.main.ORM.ClassInfoUtil;
import edu.brown.cs.student.main.ORM.FieldInfo;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * KDTree is a data structure that can be thought of a BST for n different axes to sort on.
 */
final class KDTree {

  private final Node root;

  /**
   * a KDTree must take in a collection of objects that it wants to store, as well as how
   * many dimensions it will need to partition. This construcutor is called recursively
   *
   * @param vals       : collection of objects
   * @param dimensions : how many axes we want our KD-Tree to sort on
   */
  <T extends Comparable<T>> KDTree(Collection<T> vals, int dimensions) {
    //Check valid input
    if (vals == null) {
      throw new IllegalArgumentException("The collection of values that you provide to the KDTree"
          + "constructor cannot be null");
    }
    if (dimensions == 0) {
      throw new IllegalArgumentException("You must provide more than 0 dimensions for your"
          + "KDTree to sort on.");
    }

    //Convert collections of objects to a list of KDSortableByAxis objects, so that we can compare
    //them by axis later.
    List<SortableByAxis<T>> values = new ArrayList<>();
    for (T val : vals) {
      SortableByAxis<T> s = new KDSortableByAxis<>(val);
      values.add(s);
    }

    //Sort the values based on the relevant axis. This should sort based on the relevant axis.
    // In this case, the axis would be 0.
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

    //Reset the axis we're comparing on for the SortableByAxis objects
    for (SortableByAxis<T> val : vals) {
      val.setAxis(depth % dimensions);
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
   * @param propertyIndices : indices for properties we care about.
   * @return k nearest neighbors
   */
  private List<Object> kNearestNeighbors(Object target, int k,
                                         List<Integer> propertyIndices)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    //Create nearestNeighbors list and get nearest neighbors
    List<Node> nearestNeighbors = new ArrayList<>();
    nearestNeighbors = kNN(root, target, k, propertyIndices, nearestNeighbors);

    //Convert nearestNeighbors from list of Nodes to list of Objects
    List<Object> nearestObjects = new ArrayList<>();
    for (Node node : nearestNeighbors) {
      nearestObjects.add(node.getVal());
    }

    //Return k nearest objects
    return nearestObjects;
  }

  /**
   * kNN takes in an Object and finds the k nearest neighbors to that Object based
   * on a list of given properties. SEE HANDOUT: I copied pseudocode from there.
   * Doc: https://docs.google.com/document/d/1TYUbEgyh7oQEt0nW9pmGO-_PEmnkuGqBfuItbuqFk6s/edit
   *
   * @param target          : Object we're comparing against
   * @param k               : number of nearest neighbors.
   * @param propertyIndices : indices for properties we care about.
   * @return list of k nearest Nodes.
   */
  private List<Node> kNN(Node node, Object target, int k,
                         List<Integer> propertyIndices, List<Node> nearestNeighbors)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    //Base case: null Node.
    if (node == null) {
      return nearestNeighbors;
    }

    //    Get the straight-line (Euclidean) distance from your target point to the current node.
    double distance = node.getEuclideanDistance(target, propertyIndices);

//    If the current node is closer to your target point than one of your k-nearest neighbors,
//    or if your collection of neighbors is not full, update the list accordingly
    int size = nearestNeighbors.size();

    if (size == 0) {
      nearestNeighbors.add(node);
    } else if (size < k) {
      int pos = getNodePosition(target, propertyIndices, nearestNeighbors, node);
      nearestNeighbors.add(pos, node);
    } else {
      Node furthestNeighbor = nearestNeighbors.get(0);
      if (distance
          < furthestNeighbor.getEuclideanDistance(target, propertyIndices)
      ) {
        nearestNeighbors.remove(0);
        int pos = getNodePosition(target, propertyIndices, nearestNeighbors, node);
        nearestNeighbors.add(pos, node);
      }
    }

    // find relevant axis by depth. We know that the number of axes is equal to the number of
    // properties given.
    int axis = node.getDepth() % propertyIndices.size();

    //TODO: have to be really careful with property indices and axis. The target object will only
    // have n axes, but we might want a property at n + 1
//    If the Euclidean distance between the target point and the farthest of the current
//    “best neighbors” you have so far is greater than the relevant axis distance* between
//    the current node and target point, recur on both children.
    Node furthestNeighbor = nearestNeighbors.get(0);
    double furthestNeighborDistance =
        furthestNeighbor.getEuclideanDistance(target, propertyIndices);

    double relevantAxisDistance = node.getAxisDistance(target, axis);

    //recur on both children. Second part of OR operator is found at "**Note:" in handout
    if (furthestNeighborDistance > relevantAxisDistance || nearestNeighbors.size() < k) {
      nearestNeighbors = kNN(node.getRight(), target, k, propertyIndices, nearestNeighbors);
      nearestNeighbors = kNN(node.getLeft(), target, k, propertyIndices, nearestNeighbors);
    } else {
//      If the current node's coordinate on the relevant axis is less than the target's coordinate
//      on the relevant axis, recur on the right child.
      int targetCoordinate = getTargetCoordinate(target, axis);
      if (node.getCoordinate(axis) < getTargetCoordinate(target, axis)) {
        nearestNeighbors = kNN(node.getRight(), target, k, propertyIndices, nearestNeighbors);
      } else {
//      Else if the current node's coordinate on the relevant axis is greater than the target's
//      coordinate on the relevant axis, recur on the left child.
        nearestNeighbors = kNN(node.getLeft(), target, k, propertyIndices, nearestNeighbors);
      }
    }
    return nearestNeighbors;
  }

  /**
   * Helper method invoked in kNN() that gets the target coordinate for a specific property.
   *
   * @param target: Object we're comparing against
   * @param axis:   relevant axis
   * @return coordinate
   */
  private int getTargetCoordinate(Object target, int axis)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    List<FieldInfo> targetFields = ClassInfoUtil.populateFieldInfo(target.getClass());
    return (Integer) targetFields.get(axis).readMethod.invoke(target);
  }

  /**
   * This helper method returns the position of a Node in a list. It is used in kNN()
   * to update the Queue when it has not yet reached k, where k is the number of nearest neighbors
   * that we want to return.
   *
   * @param propertyIndices:  indices for properties we care about.
   * @param nearestNeighbors: list of nearest neighbors.
   * @param node:             node that we want to find the position for.
   * @return position in list
   */
  private int getNodePosition(Object target, List<Integer> propertyIndices,
                              List<Node> nearestNeighbors,
                              Node node)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    for (int i = 0; i < nearestNeighbors.size(); i++) {
      Node furthestNode = nearestNeighbors.get(i);
      if (furthestNode.getEuclideanDistance(target, propertyIndices)
          < node.getEuclideanDistance(target, propertyIndices)) {
        return i;
      }
    }
    return nearestNeighbors.size();
  }

  /**
   * classify takes in an object, and uses the k nearest neighbors
   * to determine what property that object would likely have.
   *
   * @param target : target object we are comparing against
   * @param k : number of nearest neighbors we will use to classify.
   * @param propertyIndex : index for property we care about
   * @return classification
   */
  private Object classify(Object target, int k, int propertyIndex)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    List<Integer> properties = new ArrayList<>(List.of(propertyIndex));
    List<Object> nearestNeighbors = kNearestNeighbors(target, k, properties);
    return null;
  }

  }
/*
  My current questions:
  1) How do we know what properties we're going to test on if this is generic?
  <p>
  => Benji: By properties, I assume you mean the given user_id, weight, height, or age. I think
  we use the values passed via the command line to either find (if given a user_id)
  or construct (if given 1-3 of weight, height, age) a node that we search around.
  <p>
  i.e.
  (1) similar 5 151944 => similar <k> <user_id> => find existing node
  (2) similar 5 190 70 21 => similar <k> <weight> <height> <age> => create new node
  <p>
  So to make this generic, we'd assume that we could get any number of int arguments
  representing some properties via the REPL.
  <p>
  (3) similar 5 190 70 21 40 50 => similar <k> <prop1> <prop2> <prop3> <prop4> <prop5>
  <p>
  Here we'd construct a 5 dimensional node with props 1 through 5.
  <p>
  2) What exactly is target? Are we given a Node? Are we given a list of ints? If it's the latter,
  how do we know what we're comparing to?
  <p>
  => Benji: I think we are given either
  (1) a user_id, or
  (2) 1-3 of {weight in lbs, height in inches, age in years}
  <p>
  So in our KD tree we need to either
  (1) find the node that corresponds to the given user_id, or
  (2) construct a node given 1-3 dimensions of data
  That is our *target*.
  <p>
  I think the input *node* is simply the root node of our KD tree that we begin our search from.
  <p>
  3) Why am I having issues with Node not being a concrete class?
  <p>
  Benji: you mean that not all methods can be/are implemented in Node?
  I don't think we need to calculate euclidean distance for n dimensions? That would
  get pretty involved computationally.
  <p>
  4) What's the level of generality that we're looking for? Can my Node store a SQL row? Or can
  it not know what it's storing?
  <p>
  I think it should store an object representation of a SQL row. So essentially an object
  with an arbitrary number of fields. The Node object itself wouldn't know what it's storing
  unless we use Java's reflection API like in the ORM to extract its fields.
 */
















