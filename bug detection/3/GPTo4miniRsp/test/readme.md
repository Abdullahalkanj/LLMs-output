
---

##  How to Compile and Run the Tests

This project includes a `run_test.bat` script to compile and run Java tests using JUnit.

### Requirements

* **Java JDK** installed and added to your system PATH.
* JUnit libraries located in the `lib/` folder:

  * `junit-4.13.2.jar`
  * `hamcrest-core-1.3.jar`
* **Visual Studio Code** installed (optional but recommended).
* **[Code Runner Extension](https://marketplace.visualstudio.com/items?itemName=formulahendry.code-runner)** for VS Code (if you want to run the script with a button).

### Folder Structure

```
lib/
 ├── junit-4.13.2.jar
 └── hamcrest-core-1.3.jar
3/
 └── GPTo4miniRsp/
     ├── basicprompt/
     │   └── version3/
     │       └── SIEVE.java
     └── test/
         ├── SIEVE_TEST.java
         └── QuixFixOracleHelper.java
```

### How to Run the Script

#### Option 1: Using VS Code

1. Open the project in **Visual Studio Code**.
2. Install the **Code Runner** extension (if not already installed).
3. Open the `run_test.bat` file.
4. Click the **Run ▶ button** at the top-right of the editor (or use `Ctrl + Alt + N`).
5. The script will compile the Java files and automatically run the tests.

#### Option 2: Using Command Prompt

1. Open **Command Prompt**.
2. Navigate to the directory where `run_test.bat` is located.
3. Run the script:

```sh
run_test.bat
```

### What the Script Does

* Sets up the classpath with JUnit and your source/test files.
* Compiles:

  * `SIEVE.java`
  * `SIEVE_TEST.java`
  * `QuixFixOracleHelper.java`
* Runs `SIEVE_TEST` using JUnit.
* Displays the result in the terminal.

---