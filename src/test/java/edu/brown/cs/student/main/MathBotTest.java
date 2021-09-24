package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathBotTest {

  @Test
  public void testAddition() {
    MathBot matherator9000 = new MathBot();
    double output = matherator9000.add(10.5, 3);
    assertEquals(13.5, output, 0.01);
  }

  @Test
  public void testLargerNumbers() {
    MathBot matherator9001 = new MathBot();
    double output = matherator9001.add(100000, 200303);
    assertEquals(300303, output, 0.01);
  }

  @Test
  public void testSubtraction() {
    MathBot matherator9002 = new MathBot();
    double output = matherator9002.subtract(18, 17);
    assertEquals(1, output, 0.01);
  }

  // TODO: add more unit tests of your own
  @Test
  public void testSubtractZero() {
    MathBot bot = new MathBot();
    double output = bot.subtract(18, 18);
    assertEquals(0, output, 0.01);
  }

  @Test
  public void testAddZero() {
    MathBot bot = new MathBot();
    double output = bot.add(0, 18);
    assertEquals(18, output, 0.01);
  }

  @Test
  public void testAddNegative() {
    MathBot bot = new MathBot();
    double output = bot.add(-10, 18);
    assertEquals(8, output, 0.01);
  }

  @Test
  public void testSubtractNegative() {
    MathBot bot = new MathBot();
    double output = bot.subtract(10, -18);
    assertEquals(28, output, 0.01);
  }
}
