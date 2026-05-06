# Bounce Break Java

A small desktop arcade game built in Java with Princeton's `StdDraw` library. The player launches a ball, controls a paddle, breaks a brick layout, and tries to clear the board without letting the ball fall below the paddle.

This project started as a university Java assignment and was cleaned up into a public, recruiter-friendly repository with a Maven build, explicit dependency management, and a clearer project structure.

## Highlights

- Angle-based launch mechanic with live aiming feedback
- Paddle control with keyboard input
- Brick, wall, paddle, and corner collision handling
- Score tracking, pause, game-over, victory, and restart states
- Simple rendering loop using `StdDraw`
- Maven project layout for easy cloning and running

## Skills Gained

- Java fundamentals: classes, methods, constants, arrays, records, and control flow
- Game loop design with separate input, update, and render steps
- 2D collision detection between circles and rectangles
- Vector math for ball reflection and angle-based movement
- Keyboard-driven user interaction
- State management for launch, pause, victory, game-over, and restart flows
- Maven project setup and dependency handling
- Code cleanup and refactoring from an assignment script into a maintainable project structure

## Tech Stack

- Java 17+
- Maven
- Princeton `algs4` / `StdDraw` (included in `lib/algs4.jar`)

## Controls

| Key | Action |
| --- | --- |
| Left / Right before launch | Adjust launch angle |
| Space before launch | Launch the ball |
| Left / Right after launch | Move paddle |
| Space during play | Pause / resume |
| Space after game over or victory | Restart |

## Run Locally

Make sure Java 17 or newer and Maven are installed.

```bash
mvn compile
mvn exec:java
```


## Project Structure

```text
.
├── pom.xml
├── README.md
├── lib/algs4.jar
└── src/main/java/io/github/kaanuz/bouncebreak/BounceBreakGame.java
```

## Implementation Notes

The game loop separates input handling, physics updates, and rendering. Collision detection uses circle-rectangle intersection checks for the paddle and bricks, then reflects the ball using the collision normal. Paddle hits also adjust the outgoing angle based on where the ball lands on the paddle, which gives the player more control over the next shot.

