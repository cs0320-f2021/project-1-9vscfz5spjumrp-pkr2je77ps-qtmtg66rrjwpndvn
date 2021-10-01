package edu.brown.cs.student.main;

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


}
