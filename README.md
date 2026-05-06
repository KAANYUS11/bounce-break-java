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

The `StdDraw` dependency is included as `lib/algs4.jar` from Princeton's official Algorithms library, so no separate classpath setup is required.

## Project Structure

```text
.
├── pom.xml
├── README.md
└── src
    └── main
        └── java
            └── io
                └── github
                    └── kaanuz
                        └── bouncebreak
                            └── BounceBreakGame.java
```

## Implementation Notes

The game loop separates input handling, physics updates, and rendering. Collision detection uses circle-rectangle intersection checks for the paddle and bricks, then reflects the ball using the collision normal. Paddle hits also adjust the outgoing angle based on where the ball lands on the paddle, which gives the player more control over the next shot.

`StdDraw` is provided by Princeton's Algorithms library: https://algs4.cs.princeton.edu/code/

## Suggested Repository Name

Recommended: `bounce-break-java`

Other solid options:

- `brick-breaker-java`
- `java-arcade-breakout`
- `std-draw-brick-breaker`

## License

This project is available under the MIT License.
