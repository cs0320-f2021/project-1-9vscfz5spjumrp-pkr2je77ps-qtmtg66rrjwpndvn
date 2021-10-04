package edu.brown.cs.student.main;

import org.junit.Test;
import edu.brown.cs.student.main.KDTree.*;
import edu.brown.cs.student.main.KDDummyClasses.ThreeDimTestObject;
import edu.brown.cs.student.main.KDDummyClasses.FourDimTestObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

  @Test
  public <T extends Comparable<T>> void testComplicatedConstructor() {
    ThreeDimTestObject LLC = new ThreeDimTestObject(0, 0, 3);
    ThreeDimTestObject LC = new ThreeDimTestObject(1, 1, 3);
    ThreeDimTestObject LRC = new ThreeDimTestObject(2, 2, 3);
    ThreeDimTestObject root = new ThreeDimTestObject(3, 1, 3);
    ThreeDimTestObject RC = new ThreeDimTestObject(4, 0, 3);
    ThreeDimTestObject RRC = new ThreeDimTestObject(5, 1, 3);

    Collection<T> vals = new ArrayList<>();
    List<String> fields = new ArrayList<>();
    fields.add("one");
    fields.add("two");
    fields.add("three");
    vals.add((T) root);
    vals.add((T) RC);
    vals.add((T) LC);
    vals.add((T) RRC);
    vals.add((T) LRC);
    vals.add((T) LLC);
    KDTree tree = new KDTree(vals, 3, fields);

    assert tree.getRoot() != null;
    assert tree.size() == 6;
  }

  @Test
  public <T extends Comparable<T>> void test100() {
    Collection<T> vals = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      int one = ThreadLocalRandom.current().nextInt();
      int two = ThreadLocalRandom.current().nextInt();
      int three = ThreadLocalRandom.current().nextInt();
      vals.add((T) new ThreeDimTestObject<T>(one, two, three));
    }

    List<String> fields = new ArrayList<>();
    fields.add("one");
    fields.add("two");
    fields.add("three");

    KDTree tree = new KDTree(vals, 3, fields);
    assert tree.getRoot() != null;
    assert tree.size() == 100;
  }

  @Test
  public <T extends Comparable<T>> void oneNeighbor()
      throws NoSuchFieldException, IllegalAccessException {
    ThreeDimTestObject target = new ThreeDimTestObject(0, 1, 3);
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

    List<Object> nearestNeighbor = tree.kNearestNeighbors(target, 1);
    assert nearestNeighbor.size() == 1;
    assert nearestNeighbor.get(0) == root;
  }

  @Test
  public <T extends Comparable<T>> void FiveNearestNeighborsAllZero()
      throws NoSuchFieldException, IllegalAccessException {
    Collection<T> vals = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      int one = ThreadLocalRandom.current().nextInt();
      int two = ThreadLocalRandom.current().nextInt();
      int three = ThreadLocalRandom.current().nextInt();
      vals.add((T) new ThreeDimTestObject<T>(one, two, three));
    }

    for (int i = 0; i < 5; i++) {
      vals.add((T) new ThreeDimTestObject<T>(0, 0, 0));
    }

    List<String> fields = new ArrayList<>();
    fields.add("one");
    fields.add("two");
    fields.add("three");

    KDTree tree = new KDTree(vals, 3, fields);

    ThreeDimTestObject target = new ThreeDimTestObject(0, 0, 0);
    List<Object> nearestNeighbor = tree.kNearestNeighbors(target, 5);
    assert nearestNeighbor.size() == 5;
    for (Object neighbor : nearestNeighbor) {
      assert neighbor.getClass().getField("one").getInt(neighbor) == 0;
      assert neighbor.getClass().getField("two").getInt(neighbor) == 0;
      assert neighbor.getClass().getField("three").getInt(neighbor) == 0;
    }
  }

}
