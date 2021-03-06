# Introduction
This project is a simple shell command grep app implemented using Java. 
The app searches for a text pattern recursively in a given directory, and output matched lines to a file. 
The approach at first was using java basic library to achieve the goal and then using Lambda and Stream API to refine the project.

# Usage
The program takes three argument:  
```
grep {regex} {rootPath} {outputFile}
```  
`Regex`: The pattern we want to search for in a file.  
`rootPath`: The root directory that recursively search for files that contain the target pattern.  
`outputFile`: We write our result, the line that contain the pattern into the output file.  

Example: We want to search for pattern `.*txt.*` in root directory `grep` and write result to `grep.out`  
```
grep .*txt.* ./grep tmp/grep.out
```  

# Pseudocode
The pseudocode for the project is showing below, how the project actually work:  
## Process()
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```  
First create a empty list for later storing the matching lines, Then we recursively walk thourgh our root directory by calling `listfile()`.
for each file we listed, we call `readLines()` on that file and call `containsPattern()` on each line we read from that file. After we store
all target line we call `writeToFile()` to write them into our output file.  

## listfile()
```
fileList = []
for file in rootDir
  fileList.add(file.name)
return fileList
```  
Traverse a given directory and return all files  

## readLines()
```
listString = []
for line in inputfile.readline()
  listString.add(line)
return listString
```  
Read a file and return all the lines  

## containsPattern()  
check if a line contains the regex pattern (passed by user).  

## writeToFile()
Write lines to a file.  

# Performance Issue
The program won't work on large files since we are reading line by
line and store them into the memory for further processing.
The larger file we want to process, the more memory it require.   

# Improvement
1. Due to the efficiency we improve our grep app using stream  
2. Could have more argument for the Grep class to store more imformation such as matchedLines and listfile.  
3. could make the java doc more readable