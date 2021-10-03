package edu.brown.cs.student.main;

import org.jetbrains.annotations.NotNull;

public class KDDummyClasses {
  public static class ThreeDimTestObject<T extends Comparable<T>>
      implements Comparable<ThreeDimTestObject<T>> {
    public int one;
    public int two;
    public int three;

    public ThreeDimTestObject(int one, int two, int three) {
      this.one = one;
      this.two = two;
      this.three = three;
    }

    @Override
    public int compareTo(@NotNull KDDummyClasses.ThreeDimTestObject o) {
      return 0;
    }
  }

  public static class FourDimTestObject<T extends Comparable<T>>
      implements Comparable<ThreeDimTestObject<T>> {
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

    @Override
    public int compareTo(@NotNull KDDummyClasses.ThreeDimTestObject<T> o) {
      return 0;
    }
  }
}
