package edu.brown.cs.student.main;

import edu.brown.cs.student.main.KDTree.KDNode;
import static org.junit.Assert.*;
import org.junit.Test;
import edu.brown.cs.student.main.KDDummyClasses.ThreeDimTestObject;
import edu.brown.cs.student.main.KDDummyClasses.FourDimTestObject;

public class KDNodeTest {

  public static final double EPSILON = 0.000001d;

  @Test
  public void testGetEuclideanDistanceThreeDim() throws IllegalAccessException,
      NoSuchFieldException {
    Object target = new ThreeDimTestObject(0, 0, 0);
    Object val = new ThreeDimTestObject(1, 1, 1);
    KDNode node = new KDNode( val, 0);

    double dist = node.getEuclideanDistance(target);
    assertEquals(Math.sqrt(3), dist, EPSILON);
  }

  @Test
  public void testGetEuclideanDistanceFourDim() throws IllegalAccessException,
      NoSuchFieldException {
    Object target = new FourDimTestObject(0, 0, 0, 0);
    Object val = new FourDimTestObject(1, 1, 1, 1);
    KDNode node = new KDNode( val, 0);

    double dist = node.getEuclideanDistance(target);
    assertEquals(2, dist, EPSILON);
  }

  @Test
  public void testGetEuclideanDistanceThreeVsFourDim()
      throws IllegalAccessException, NoSuchFieldException {
    Object target = new ThreeDimTestObject(0, 0, 0);
    Object val = new FourDimTestObject(1, 1, 1, 1);
    KDNode node = new KDNode( val, 0);

    double dist = node.getEuclideanDistance(target);
    assertEquals(Math.sqrt(3), dist, EPSILON);
  }

  @Test
  public void testGetEuclideanDistanceNormal()
      throws IllegalAccessException, NoSuchFieldException {
    Object target = new ThreeDimTestObject(-10, -100, 300);
    Object val = new FourDimTestObject(13, 22, 40, 230);
    KDNode node = new KDNode( val, 0);

    double dist = node.getEuclideanDistance(target);
    assertEquals(288.119766763754, dist, EPSILON);
  }

  @Test
  public void testGetAxisDistance() throws NoSuchFieldException, IllegalAccessException {
    Object target = new ThreeDimTestObject(-10, -100, 300);
    Object val = new FourDimTestObject(13, 22, 40, 230);
    KDNode node = new KDNode( val, 0);

    double dist = node.getAxisDistance(target, "two");
    assert dist == 122;
  }

  @Test
  public void testGetCoordinate() throws NoSuchFieldException, IllegalAccessException {
    Object val = new FourDimTestObject(13, 22, 40, 230);
    KDNode node = new KDNode( val, 0);

    double coord = node.getCoordinate("one");
    assert coord == 13;
  }
}
