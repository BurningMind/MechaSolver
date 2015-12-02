@echo off

del *.class
rmdir /q /s build

dir /s /B src\*.java > sources.txt

javac -d .\ @sources.txt

mkdir build
jar cfm build\MechaSolver.jar Manifest.txt *.class
