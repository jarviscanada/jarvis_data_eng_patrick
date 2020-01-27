package ca.jrvs.apps.grep;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepLambdaImp extends JavaGrepImp {

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
      ex.printStackTrace();
    }
  }

  /**
   * Implement using lambda and stream APIs
   */
  @Override
  public List<File> listFiles(String rootDir) {
    File root = new File(rootDir);
    Path p = root.toPath();
    try {
      return Files.walk(p).map(Path::toFile).collect(Collectors.toList());
    } catch (IOException e){
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Implement using lambda and stream APIs
   */
  @Override
  public List<String> readLines(File inputFile) {
    Path p = inputFile.toPath();
    try {
      return Files.lines(p).collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
