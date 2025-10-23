@echo off
echo ========================================
echo Bug Tracker - Database Viewer
echo ========================================
echo.
echo Loading database contents...
echo.
java -cp "bin;lib/*" util.DatabaseViewer
echo.
echo ========================================
echo Press any key to exit...
pause >nul
