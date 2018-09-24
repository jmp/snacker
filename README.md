# Snacker

[![Build Status](https://travis-ci.org/jmp/snacker.svg?branch=master)](https://travis-ci.org/jmp/snacker)

A small time tracker written in Java. It is currently very limited,
but can do basic time tracking of tasks and export the results into
a CSV file.

![Example animation][example]

## Compiling

Build with Gradle:

    gradlew build

This creates an executable JAR at `build/libs/snacker.jar`.

## Usage

Run the executable JAR:

    java -jar snacker.jar

[example]: https://raw.githubusercontent.com/jmp/snacker/master/example.gif
