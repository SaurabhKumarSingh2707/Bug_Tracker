@echo off
echo Downloading SQLite JDBC driver...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar' -OutFile 'lib\sqlite-jdbc-3.44.1.0.jar'"
echo Download complete!
pause
