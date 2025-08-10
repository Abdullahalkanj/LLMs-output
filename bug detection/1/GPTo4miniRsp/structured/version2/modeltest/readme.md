# MemberManager Unit Tests

This folder contains unit tests for the `MemberManager` class using **JUnit 5**, placed in the `modeltest/` folder.

---

## Prerequisites

Before running the tests, ensure you have the following:

- Java Development Kit (JDK) installed and configured (`javac`, `java`)
- [JUnit 5 Console Standalone JAR](https://search.maven.org/artifact/org.junit.platform/junit-platform-console-standalone)
- All project `.java` files located under `/model` and `/modeltest`
- External JARs placed in a `/lib` directory:
  - `junit-jupiter-api-5.10.0.jar`
  - `junit-platform-console-standalone-1.10.0.jar`

---

## How to Compile and Run the Unit Tests

Follow these steps to compile the project and run the tests from the command line.

---

### Step 1: Open a terminal and navigate to the project root
cd \1\GPTo4miniRsp\structured\version2

### Step 2: Compile all source and test files to the /bin folder
On Windows:
javac -d bin -cp "lib/*" model2/*.java modeltest/*.java
javac -d bin -cp "lib/junit-platform-console-standalone-1.10.0.jar" model2/*.java modeltest/*.java


On macOS/Linux:
javac -d bin -cp "lib/*" model/*.java modeltest/*.java

### Step 3: Run the tests using JUnit Console Runner
java -jar lib/junit-platform-console-standalone-1.10.0.jar --class-path bin --scan-classpath --details verbose

