@echo off
echo ================================================
echo Bug Tracker - Compilation Script
echo ================================================
echo.

echo Compiling Java files...
javac -cp "lib/*" -d bin src/main/*.java src/model/*.java src/database/*.java src/service/*.java src/ui/*.java

if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo Compilation successful!
    echo ================================================
    echo.
    echo To run the application, use:
    echo java -cp "bin;lib/*" main.Main
    echo.
) else (
    echo.
    echo ================================================
    echo Compilation failed! Please check the errors above.
    echo ================================================
    echo.
)

pause
