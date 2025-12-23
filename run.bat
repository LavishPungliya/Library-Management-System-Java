@echo off
echo Compiling Java files...
javac *.java
if %errorlevel% neq 0 (
    echo Compilation failed. Press any key to exit.
    pause >nul
    exit /b
)

echo Running Library Management System...
java LibrarySystem
pause