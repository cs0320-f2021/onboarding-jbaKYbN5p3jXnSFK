package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

  @Test
  public void testNegative(){
    MathBot matherator0 = new MathBot();
    double output = matherator0.subtract(2, 20);
    assertEquals(-18.0, output, 0.01);
  }
/*
  @Test
  public void testInput(){
    try {
      MathBot matherator1 = new MathBot();
      double output = matherator1.subtract(2, Double.parseDouble("yellow"));
      fail("Exception not thrown");
    }
    catch(IllegalArgumentException ex) {
      assertEquals("Error: Inputs are not numbers", ex.getMessage());
    } //getMessage wont work because it jumped to the catch block
  }
  */

  @Test
  public void testSameNumber(){
      MathBot matherator1 = new MathBot();
      double output = matherator1.subtract(2, 2);
      assertEquals(0, output, 0.01);
    }

  @Test
  public void testZero(){
    MathBot matherator2 = new MathBot();
    double output = matherator2.add(0, 5);
    assertEquals(5, output, 0.01);
  }

  @Test
  public void testDecimal(){
    MathBot matherator3 = new MathBot();
    double output = matherator3.add(0.004, 7);
    assertEquals(7.004, output, 0.01);
  }




  // TODO: add more unit tests of your own
}
