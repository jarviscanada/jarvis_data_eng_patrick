package ca.jrvs.apps.practice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegexExcImpTest {
  private RegexExcImp text;
  @Before
  public void setUp() throws Exception {
    text = new RegexExcImp();
  }

  @After
  public void tearDown() throws Exception {
    text = null;
  }

  @Test
  public void testMatchJpeg() {
    Assert.assertTrue(text.matchJpeg("asdasdsd.jpg"));
    Assert.assertTrue(text.matchJpeg("asdasdsd.jpeg"));
    Assert.assertFalse(text.matchJpeg("aaaasasas"));
  }

  @Test
  public void testMatchIp() {
    Assert.assertTrue(text.matchIp("1.3.4.5"));
  }

  @Test
  public void testIsEmptyLine() {
    Assert.assertTrue(text.isEmptyLine("             "));
    Assert.assertFalse(text.isEmptyLine("     www        "));
  }
}