package edu.brown.cs.student.main;

import org.jetbrains.annotations.NotNull;

public class KDDummyClasses {
  public static class ThreeDimTestObject{
    public int one;
    public int two;
    public int three;

    public ThreeDimTestObject(int one, int two, int three) {
      this.one = one;
      this.two = two;
      this.three = three;
    }
  }

  public static class FourDimTestObject {
    public int one;
    public int two;
    public int three;
    public int four;

    public FourDimTestObject(int one, int two, int three, int four) {
      this.one = one;
      this.two = two;
      this.three = three;
      this.four = four;
    }
  }
}
