@echo off
setlocal
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\bootstrap-maven.ps1"
if errorlevel 1 exit /b 1
call "%~dp0.tools\apache-maven-3.9.9\bin\mvn.cmd" "-Dmaven.repo.local=%~dp0.tools\repository" -f "%~dp0pom.xml" %*
exit /b %errorlevel%
