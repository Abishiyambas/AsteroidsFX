# AsteroidsFX

## Lab Exercises

| Lab Exercise | Implemented In |
| --- | --- |
| IntroLab | Project structure in `Core/`, `Player/`, `Bullet/`, `Enemy/`, `Asteroids/`, `Collision/`, and `ScoringService/` |
| JavaLab | Java implementation across all modules, especially `Core/src/main/java/` and the gameplay modules |
| GamesLab | Game implementation in `Core/`, `Player/`, `Bullet/`, `Enemy/`, `Asteroids/`, and `Collision/` |
| JPMSLab1 | Module structure through `module-info.java` in `Core/`, `Player/`, `Bullet/`, `Enemy/`, `Asteroids/`, and `Collision/` |
| JPMSLab2 | Service-based architecture with `ServiceLoader` in `Core/src/main/java/dk/sdu/cbse/PluginServiceRegistry.java` and `Player/src/main/java/dk/sdu/cbse/player/PlayerControlSystem.java` |
| JPMSLab3 | Runtime plugin loading with `ModuleLayer` in `Core/src/main/java/dk/sdu/cbse/PluginServiceRegistry.java` and plugin jars in `plugins/` |
| SpringLab | Spring integration in `Core/src/main/java/dk/sdu/cbse/App.java`, `Core/src/main/java/dk/sdu/cbse/CoreSpringConfig.java`, `Core/src/main/java/dk/sdu/cbse/ScoringClient.java`, and `ScoringService/` |
| Testing | Tests in `Core/src/test/java/`, `Player/src/test/java/`, `Asteroids/src/test/java/`, `Collision/src/test/java/`, and `ScoringService/src/test/java/` |
| MicroServices | Microservice implementation in `ScoringService/` with client integration in `Core/src/main/java/dk/sdu/cbse/ScoringClient.java` |
| ModularMicroServices | Modular game plus microservice split across `Core/`, the gameplay modules, and `ScoringService/` |

## Requirements

- Java 17 or newer
- Maven 3.8 or newer
- A desktop environment that can open JavaFX

## Build

Run from the project root:

```bash
mvn clean test package
```

## Start the Game

Run from the project root:

```bash
mvn exec:exec
```

## Start the Microservice

Run from the project root:

```bash
mvn -pl ScoringService spring-boot:run
```
