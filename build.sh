#!/bin/sh

# Generate lexer, parser classes and base visitor class
java -cp ".:lib/antlr-4.7-complete.jar:$CLASSPATH" org.antlr.v4.Tool Simple.g4 -visitor -o gen


# Compile all
javac -cp ".:lib/antlr-4.7-complete.jar:$CLASSPATH" -sourcepath "src:gen" -d "build" src/*.java
