@echo off
setlocal

echo Compiling Java source files...

REM Set the classpath to include JUnit and both class locations
set CP=lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar;3\GPTo4miniRsp\structuredpromt\version3;3\GPTo4miniRsp\test

javac -cp %CP% ^
  3\GPTo4miniRsp\structuredpromt\version3\SIEVE.java ^
  3\GPTo4miniRsp\test\SIEVE_TEST.java ^
  3\GPTo4miniRsp\test\QuixFixOracleHelper.java

if %errorlevel% neq 0 (
    echo ❌ Compilation failed.
    pause
    exit /b 1
)

echo ✅ Running tests...
java -cp %CP% org.junit.runner.JUnitCore SIEVE_TEST

pause
endlocal
