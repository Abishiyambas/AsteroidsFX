# AsteroidsFX – Startup Guide

This guide helps new users build and run the project.

## Requirements
- Java 17+ (JavaFX is loaded via Maven dependencies)
- Maven 3.8+

## Build
From the project root:
```bash
mvn clean install
```
This compiles all modules and copies JavaFX and module jars into `mods-mvn/`.

## Run
From the project root:
```bash
mvn exec:exec
```


