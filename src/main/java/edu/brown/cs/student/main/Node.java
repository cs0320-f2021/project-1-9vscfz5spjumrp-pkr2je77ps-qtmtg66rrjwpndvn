package edu.brown.cs.student.main;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

interface Node {
  Node left = null;
  Node right = null;
  Object val = null;
  int depth = 0;

  Node getLeft();

  Node getRight();

  Object getVal();

  int getDepth();

  void setRight(Node rightChild);

  void setLeft(Node leftChild);

  double getEuclideanDistance(Object target, List<Integer> propertyIndices)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException;

  double getAxisDistance(Object target, int propertyIndex)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException;

  int getCoordinate(int propertyIndex)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException;


}
