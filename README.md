# MechaSolver
---
## Building
Execute `build.cmd` or run `javac` recursively on the `src/` folder to generate the Java classes, then pack them from the root directory with:
```
mkdir build
jar cfm build/MechaSolver.jar Manifest.txt *.class
```

## Running
```
java -jar build/MechaSolver.jar
```
