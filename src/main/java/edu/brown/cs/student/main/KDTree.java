package edu.brown.cs.student.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * KDTree is a data structure that can be thought of a BST for n different things to sort on.
 */
final class KDTree {

  private Node root = null;


  /**
   * a KDTree must take in a list of objects that it wants to store, as well as how
   * many dimensions it will need to partition. This construcutor is called recursively
   *
   * @param vals
   * @param depth
   * @param dimensions
   */
  <T extends Comparable<T>> KDTree(List<T> vals, int depth, int dimensions) {
    //check for invalid inputs
    if (vals == null || dimensions == 0) {
      throw new IllegalArgumentException("You did not provide valid inputs to the KD"
          + "Tree Constructor. Either your inputed lists of vals was null or you wanted "
          + "to sort on 0 dimensions");
    }

    //TODO: implement comparable method so that we can sort on correct axis
    int axis = depth % dimensions;
    Collections.sort(vals);

    //get the middle element
    int median = vals.size() / 2;
    T val = vals.get(median);

    //create a node out of that middle element
    Node node = new Node(val, depth);

    //set root
    root = node;

    //set children
    node.setLeft(kDTreeBuilder(vals.subList(0, median), depth + 1, dimensions));
    node.setRight(kDTreeBuilder(vals.subList(median, vals.size()), depth + 1, dimensions));
  }

  /**
   * This is a helper method that recursively builds our KDTree.
   *
   * @param vals:       list of items left to insert
   * @param depth:      how deep we are in the KDTree
   * @param dimensions: how many dimensions we have
   */
  private <T extends Comparable<? super T>> Node kDTreeBuilder(List<T> vals, int depth, int dimensions) {
    //check for invalid inputs
    if (vals == null || dimensions == 0) {
      throw new IllegalArgumentException("You did not provide valid inputs to the KD"
          + "Tree Constructor. Either your inputed lists of vals was null or you wanted "
          + "to sort on 0 dimensions");
    }

    //Base case: return if there is nothing left to sort
    if (vals.size() == 0) {
      return null;
    }

    //TODO: implement comparable method so that we can sort on correct axis
    int axis = depth % dimensions;
    Collections.sort(vals);

    //get the middle element
    int median = vals.size() / 2;
    T val = vals.get(median);

    //create a node out of that middle element
    Node node = new Node(val, depth);

    //set children
    node.setLeft(kDTreeBuilder(vals.subList(0, median), depth + 1, dimensions));
    node.setRight(kDTreeBuilder(vals.subList(median, vals.size()), depth + 1, dimensions));
    return node;
  }

  /**
   * kNearestNeighbors takes in an Object and finds the k nearest neighbors to that Object based
   * on a list of given properties.
   * <p>
   * TODO: change target type to something else.
   *
   * @param node
   * @param k
   * @param propertyIndices
   * @param <T>
   * @return
   */
  private <T extends Comparable<T>> List<Node<T>> kNearestNeighbors(Node node, Node target, int k,
                                                                    List<Integer> propertyIndices) {
    List<Node<T>> nearestNeighbors = new ArrayList<>();

    //get Euclidian distance
    /**
     * My current questions:
     * 1) How do we know what properties we're going to test on if this is generic?
     *
     * => Benji: By properties, I assume you mean the given user_id, weight, height, or age. I think
     *           we use the values passed via the command line to either find (if given a user_id)
     *           or construct (if given 1-3 of weight, height, age) a node that we search around.
     *
     *           i.e.
     *           (1) similar 5 151944 => similar <k> <user_id> => find existing node
     *           (2) similar 5 190 70 21 => similar <k> <weight> <height> <age> => create new node
     *
     *           So to make this generic, we'd assume that we could get any number of int arguments
     *           representing some properties via the REPL.
     *
     *           (3) similar 5 190 70 21 40 50 => similar <k> <prop1> <prop2> <prop3> <prop4> <prop5>
     *
     *           Here we'd construct a 5 dimensional node with props 1 through 5.
     *
     * 2) What exactly is target? Are we given a Node? Are we given a list of ints? If it's the latter,
     * how do we know what we're comparing to?
     *
     * => Benji: I think we are given either
     *    (1) a user_id, or
     *    (2) 1-3 of {weight in lbs, height in inches, age in years}
     *
     *    So in our KD tree we need to either
     *    (1) find the node that corresponds to the given user_id, or
     *    (2) construct a node given 1-3 dimensions of data
     *    That is our *target*.
     *
     *    I think the input *node* is simply the root node of our KD tree that we begin our search from.
     *
     * 3) Why am I having issues with Node not being a concrete class?
     *
     *    Benji: you mean that not all methods can be/are implemented in Node?
     *    I don't think we need to calculate euclidean distance for n dimensions? That would
     *    get pretty involved computationally.
     *
     * 4) What's the level of generality that we're looking for? Can my Node store a SQL row? Or can
     * it not know what it's storing?
     *
     *    I think it should store an object representation of a SQL row. So essentially an object
     *    with an arbitrary number of fields. The Node object itself wouldn't know what it's storing
     *    unless we use Java's reflection API like in the ORM to extract its fields.
     */
    double distance = node.getEuclidianDistance(target, propertyIndices);

    //update nearestNeighbors if under limit or if this is a better neighbor

    // find relevant axis by depth

    // figure out if you need to recur on both children

    // if you didn't recur on both children, find out which child to recur on

    return null;
  }

  /**
   * classify takes in an object, and uses the k nearest neighbors
   * to determine what property that object would likely have.
   *
   * @param val
   * @param k
   * @param property
   * @return
   */
  private <T extends Comparable<T>> T classify(T val, int k, T property) {
    return null;
  }
}

















