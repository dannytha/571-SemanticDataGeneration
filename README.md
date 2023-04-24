# 571 - Semantic Web Technologies

## Project 3: Semantic Data Querying using SPARQL

### Description
This repository hosts the application and source to load RDF/OWL files, as well as a SPARQL query file,
and return the results of the SPARQL query via the command line. The results are also saved to a text file in the working
directory as well.

### Running the Query Application
Verifying that Java 19 is installed, the program can be run by executing the provided SemQuery_UNIX.sh or SemQuery_Windows.bat, depending on the host OS. These executable scripts return results in a CLI. In the case that the user wants to run with the command line, executing the provided 571-SemanticDataGeneration.jar should work as well. The results will be saved to a the 'queryResults.txt' file.

## Project 1: Generate an RDF from a CSV dataset.
#### **Language: Java**
###### Tested with:
```Eclipse```  
```JDK 19``` 

## **Steps to run with MAVEN:**
To import the project in Eclipse:
> - File > Import > Maven
> - Click "Existing Maven Project"
> - Navigate to the location of the "pom.xml" file of this folder
> - Checkbox the file and click 'Finish' to import

#### Entry at 'main/App.java'
##### 'SemanticConverter.java' holds all necessary functions.  
##### RDFS or CSV URL can be provided in constructor.  
##### RDF outputs will be placed in the '/outputs' folder.


