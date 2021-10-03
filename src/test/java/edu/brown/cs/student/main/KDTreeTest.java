package edu.brown.cs.student.main;

import org.junit.Test;
import edu.brown.cs.student.main.KDTree.*;
import edu.brown.cs.student.main.KDDummyClasses.ThreeDimTestObject;
import edu.brown.cs.student.main.KDDummyClasses.FourDimTestObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KDTreeTest {

  @Test
  public <T extends Comparable<T>> void testConstructor() {
    ThreeDimTestObject root = new ThreeDimTestObject(1, 1, 3);
    ThreeDimTestObject leftChild = new ThreeDimTestObject(0, 2, 3);
    ThreeDimTestObject rightChild = new ThreeDimTestObject(2, 2, 3);
    Collection<T> vals = new ArrayList<>();
    List<String> fields = new ArrayList<>();
    fields.add("one");
    fields.add("two");
    vals.add((T) root);
    vals.add((T) leftChild);
    vals.add((T) rightChild);
    KDTree tree = new KDTree(vals, 2, fields);

    assert tree.getRoot() != null;
  }

}
