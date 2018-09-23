# Snacker

A small time tracker written in Java. It is currently very limited,
but can do basic time tracking of tasks and export the results into
a CSV file.

![Example animation][example]

## Compiling

First, compile it with `javac`:

    javac *.java

Then, create an executable JAR:

    jar -cfe snacker.jar Snacker *.class main.fxml style.css

## Usage

Run the executable JAR:

    java -jar snacker.jar 

[example]: https://raw.githubusercontent.com/jmp/snacker/master/example.gif
