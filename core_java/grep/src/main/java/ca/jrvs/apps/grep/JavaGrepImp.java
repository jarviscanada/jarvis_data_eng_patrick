package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaGrepImp implements JavaGrep {

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String [] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: regex rootpath outFile");
    }
    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      throw new RuntimeException("Unable to process");
    }
  }

  /**
   * Top level search workflow
   *
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    List<String> matchedLine = new ArrayList<>();
    List<File> listFile = listFiles(getRootPath());
    for (File file: listFile) {
      for (String line : readLines(file)) {
        if (containsPattern(line)) {
          matchedLine.add(line);
        }
      }
    }
    writeToFile(matchedLine);
  }

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) {
    File root = new File(rootDir);
    File[] list = root.listFiles();
    List<File> fileList = new ArrayList<>();
    if (list == null) {
      return null;
    }
    for (File f : list) {

      if (f.isDirectory()) {
        List<File> tempF = listFiles(f.getAbsolutePath());
        fileList.addAll(tempF);
      } else {
        fileList.add(f);
      }
    }

    return fileList;
  }

  /**
   * Read a file and return all the lines
   * <p>
   * Explain FileReader, BufferedReader, and character encoding
   *
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  @Override
  public List<String> readLines(File inputFile) {
    List<String> listString = new ArrayList<>();
    try {
      Scanner scanner = new Scanner(inputFile);
      while (scanner.hasNextLine()) {
        listString.add(scanner.nextLine());
      }
      scanner.close();
    } catch (IOException e) {
      throw new RuntimeException("unable to readline");
    }
    return listString;
  }

  /**
   * check if a line contains the regex pattern (passed by user)
   *
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {
    return line.matches(this.getRegex());
  }

  /**
   * Write lines to a file
   * <p>
   * Explore: FileOutputStream, OutputStreamWriter, and BufferedWriter
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(this.getOutFile()));
    for (String line: lines) {
      writer.write(line);
      writer.newLine();
    }
    writer.close();
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
