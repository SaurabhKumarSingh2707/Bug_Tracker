@echo off
echo ========================================
echo  Compiling Bug Tracker Application
echo ========================================
echo.

cd src

echo Compiling model classes...
javac com\bugtracker\model\*.java

echo Compiling util classes...
javac com\bugtracker\util\*.java

echo Compiling service classes...
javac com\bugtracker\service\*.java

echo Compiling view classes...
javac com\bugtracker\view\*.java

echo Compiling main application...
javac com\bugtracker\BugTrackerApp.java

echo.
echo ========================================
echo  Compilation Complete!
echo ========================================
echo.
echo To run the application, execute: run.bat
echo.
pause
