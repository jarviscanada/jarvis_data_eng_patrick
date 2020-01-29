package ca.jrvs.apps.grep;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JavaGrepImpTest {

  JavaGrepImp grepImp;
  String inputFile;
  String outputFile;
  String root;
  String regex;
  ArrayList<File> testList;
  ArrayList<String> testListName;

  @Before
  public void setUp() throws Exception {
    grepImp = new JavaGrepImp();
    inputFile = "./testing/testtxt1.txt";
    outputFile = "grep.out";
    root = "./testing";
    regex = ".*txt.*";

    testList = new ArrayList<File>();
    testList.add(new File("./testing/testtxt1.txt"));
    testList.add(new File("./testing/testtxt2.txt"));

    testListName = new ArrayList<String>();
    testListName.add("new");
    testListName.add("read");
    testListName.add("good");
    grepImp.setRegex(regex);
    grepImp.setRootPath(root);
    grepImp.setOutFile(outputFile);
  }

  @After
  public void tearDown() throws Exception {
    grepImp = null;
    inputFile = null;
    outputFile = null;
    root = null;
    regex = null;
    testList = null;
  }

  @Test
  public void listFiles() {
    assertEquals(testList, grepImp.listFiles(root));
  }

  @Test
  public void readLines() {
    assertEquals(testListName, grepImp.readLines(new File(inputFile)));
  }

  @Test
  public void containsPattern() {
    assertTrue(grepImp.containsPattern(testList.get(0).getName()));
    assertTrue(grepImp.containsPattern(testList.get(1).getName()));
  }

  @Test
  public void writeToFile() {
    try {
      grepImp.writeToFile(testListName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertEquals(grepImp.readLines(new File(inputFile)), testListName);
  }
}