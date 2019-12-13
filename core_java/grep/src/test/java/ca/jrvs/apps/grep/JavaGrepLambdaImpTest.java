package ca.jrvs.apps.grep;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JavaGrepLambdaImpTest {

  JavaGrepLambdaImp JGLI;
  String inputFile;
  String outputFile;
  String root;
  String regex;
  ArrayList<File> testList;
  ArrayList<String> testListName;

  @Before
  public void setUp() throws Exception {
    JGLI = new JavaGrepLambdaImp();
    inputFile = "grep.out";
    outputFile = "grep.out";
    root = ".";

    testList = new ArrayList<File>();
    testList.add(new File("./README.md"));
    testList.add(new File("./grep.out"));
    testList.add(new File("./pom.xml"));
    testList.add(new File("./grep.iml"));

    testListName = new ArrayList<String>();
    testListName.add("./README.md");
    testListName.add("./grep.out");
    testListName.add("./pom.xml");
    testListName.add("./grep.iml");
    JGLI.setRootPath(root);
    JGLI.setOutFile(outputFile);
  }

  @After
  public void tearDown() throws Exception {
    JGLI = null;
    inputFile = null;
    outputFile = null;
    root = null;
    regex = null;
    testList = null;
  }

  @Test
  public void listFiles() {
    assertEquals(testList, JGLI.listFiles(root));
  }

  @Test
  public void readLines() {
    assertEquals(testListName, JGLI.readLines(new File(inputFile)));
  }
}