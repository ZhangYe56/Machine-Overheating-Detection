@echo off
setlocal
cd /d "%~dp0"
call mvnw.cmd -q package
if errorlevel 1 exit /b 1
java -jar target\overheat-0.1.0.jar %*
