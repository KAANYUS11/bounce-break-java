package io.github.kaanuz.bouncebreak;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class BounceBreakGame {
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 400;
    private static final int FRAME_DELAY_MS = 10;

    private static final double BALL_RADIUS = 8.0;
    private static final double BALL_SPEED = 5.0;
    private static final double INITIAL_BALL_X = 400.0;
    private static final double INITIAL_BALL_Y = 18.0;
    private static final double INITIAL_ANGLE = 90.0;
    private static final double AIM_STEP = 1.5;
    private static final double AIM_ARROW_LENGTH = 28.0;

    private static final double PADDLE_Y = 8.0;
    private static final double PADDLE_HALF_WIDTH = 70.0;
    private static final double PADDLE_HALF_HEIGHT = 6.0;
    private static final double PADDLE_SPEED = 10.0;

    private static final double BRICK_HALF_WIDTH = 50.0;
    private static final double BRICK_HALF_HEIGHT = 10.0;
    private static final int POINTS_PER_BRICK = 10;

    private static final Font HUD_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font SCORE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private static final Font STATUS_FONT = new Font("SansSerif", Font.BOLD, 46);

    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color BALL_COLOR = new Color(15, 82, 186);
    private static final Color PADDLE_COLOR = new Color(55, 65, 81);
    private static final Color TEXT_COLOR = new Color(17, 24, 39);
    private static final Color AIM_COLOR = new Color(220, 38, 38);

    private static final Color[] BRICK_PALETTE = {
            new Color(239, 68, 68),
            new Color(249, 115, 22),
            new Color(234, 179, 8),
            new Color(34, 197, 94),
            new Color(14, 165, 233),
            new Color(99, 102, 241)
    };

    private static final double[][] BRICK_COORDINATES = {
            {50, 320}, {150, 320}, {250, 320}, {350, 320}, {450, 320}, {550, 320}, {650, 320}, {750, 320},
            {150, 300}, {250, 300}, {350, 300}, {450, 300}, {550, 300}, {650, 300},
            {50, 280}, {150, 280}, {250, 280}, {350, 280}, {450, 280}, {550, 280}, {650, 280}, {750, 280},
            {250, 260}, {350, 260}, {450, 260}, {550, 260},
            {50, 240}, {150, 240}, {250, 240}, {350, 240}, {450, 240}, {550, 240}, {650, 240}, {750, 240},
            {150, 220}, {250, 220}, {350, 220}, {450, 220}, {550, 220}, {650, 220},
            {250, 200}, {350, 200}, {450, 200}, {550, 200}
    };

    private final boolean[] bricksAlive = new boolean[BRICK_COORDINATES.length];

    private double ballX;
    private double ballY;
    private double ballVelocityX;
    private double ballVelocityY;
    private double paddleX;
    private double angle;
    private int score;
    private boolean launched;
    private boolean paused;
    private boolean gameOver;
    private boolean victory;
    private boolean spaceWasPressed;

    public static void main(String[] args) {
        new BounceBreakGame().start();
    }

    private void start() {
        configureCanvas();
        resetGame();

        while (true) {
            handleInput();
            if (launched && !paused && !gameOver && !victory) {
                updatePhysics();
            }
            render();
            StdDraw.show();
            StdDraw.pause(FRAME_DELAY_MS);
        }
    }

    private void configureCanvas() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(0.0, CANVAS_WIDTH);
        StdDraw.setYscale(0.0, CANVAS_HEIGHT);
    }

    private void resetGame() {
        Arrays.fill(bricksAlive, true);
        ballX = INITIAL_BALL_X;
        ballY = INITIAL_BALL_Y;
        ballVelocityX = 0.0;
        ballVelocityY = 0.0;
        paddleX = CANVAS_WIDTH / 2.0;
        angle = INITIAL_ANGLE;
        score = 0;
        launched = false;
        paused = false;
        gameOver = false;
        victory = false;
        spaceWasPressed = true;
    }

    private void handleInput() {
        boolean spacePressed = StdDraw.isKeyPressed(KeyEvent.VK_SPACE);
        boolean spaceJustPressed = spacePressed && !spaceWasPressed;

        if (!launched) {
            handleAiming();
            if (spaceJustPressed) {
                launchBall();
            }
        } else if (gameOver || victory) {
            if (spaceJustPressed) {
                resetGame();
            }
        } else {
            if (spaceJustPressed) {
                paused = !paused;
            }
            if (!paused) {
                movePaddle();
            }
        }

        spaceWasPressed = spacePressed;
    }

    private void handleAiming() {
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            angle = Math.min(180.0, angle + AIM_STEP);
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            angle = Math.max(0.0, angle - AIM_STEP);
        }
    }

    private void launchBall() {
        ballVelocityX = BALL_SPEED * Math.cos(Math.toRadians(angle));
        ballVelocityY = BALL_SPEED * Math.sin(Math.toRadians(angle));
        launched = true;
    }

    private void movePaddle() {
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            paddleX = Math.max(PADDLE_HALF_WIDTH, paddleX - PADDLE_SPEED);
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            paddleX = Math.min(CANVAS_WIDTH - PADDLE_HALF_WIDTH, paddleX + PADDLE_SPEED);
        }
    }

    private void updatePhysics() {
        ballX += ballVelocityX;
        ballY += ballVelocityY;

        if (ballX <= BALL_RADIUS || ballX >= CANVAS_WIDTH - BALL_RADIUS) {
            ballVelocityX = -ballVelocityX;
            ballX = clamp(ballX, BALL_RADIUS, CANVAS_WIDTH - BALL_RADIUS);
        }
        if (ballY >= CANVAS_HEIGHT - BALL_RADIUS) {
            ballVelocityY = -ballVelocityY;
            ballY = CANVAS_HEIGHT - BALL_RADIUS;
        }
        if (ballY < -BALL_RADIUS) {
            gameOver = true;
            stopBall();
            return;
        }

        handlePaddleCollision();
        handleBrickCollisions();
    }

    private void handlePaddleCollision() {
        if (ballVelocityY >= 0) {
            return;
        }

        Collision collision = detectCircleRectangleCollision(paddleX, PADDLE_Y, PADDLE_HALF_WIDTH, PADDLE_HALF_HEIGHT);
        if (!collision.hit()) {
            return;
        }

        double hitOffset = (ballX - paddleX) / PADDLE_HALF_WIDTH;
        double bounceAngle = Math.toRadians(90.0 - hitOffset * 55.0);
        ballVelocityX = BALL_SPEED * Math.cos(bounceAngle);
        ballVelocityY = Math.abs(BALL_SPEED * Math.sin(bounceAngle));
        ballY = PADDLE_Y + PADDLE_HALF_HEIGHT + BALL_RADIUS;
    }

    private void handleBrickCollisions() {
        for (int i = 0; i < BRICK_COORDINATES.length; i++) {
            if (!bricksAlive[i]) {
                continue;
            }

            double brickX = BRICK_COORDINATES[i][0];
            double brickY = BRICK_COORDINATES[i][1];
            Collision collision = detectCircleRectangleCollision(brickX, brickY, BRICK_HALF_WIDTH, BRICK_HALF_HEIGHT);
            if (!collision.hit()) {
                continue;
            }

            bricksAlive[i] = false;
            score += POINTS_PER_BRICK;
            reflectBall(collision);

            if (score == BRICK_COORDINATES.length * POINTS_PER_BRICK) {
                victory = true;
                stopBall();
            }
            return;
        }
    }

    private Collision detectCircleRectangleCollision(double rectX, double rectY, double halfWidth, double halfHeight) {
        double closestX = clamp(ballX, rectX - halfWidth, rectX + halfWidth);
        double closestY = clamp(ballY, rectY - halfHeight, rectY + halfHeight);
        double distanceX = ballX - closestX;
        double distanceY = ballY - closestY;
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        if (distanceSquared > BALL_RADIUS * BALL_RADIUS) {
            return Collision.none();
        }

        if (distanceSquared == 0.0) {
            double horizontalOverlap = halfWidth - Math.abs(ballX - rectX);
            double verticalOverlap = halfHeight - Math.abs(ballY - rectY);
            return Math.abs(horizontalOverlap) < Math.abs(verticalOverlap)
                    ? new Collision(true, Math.signum(ballX - rectX), 0.0)
                    : new Collision(true, 0.0, Math.signum(ballY - rectY));
        }

        double distance = Math.sqrt(distanceSquared);
        return new Collision(true, distanceX / distance, distanceY / distance);
    }

    private void reflectBall(Collision collision) {
        double normalX = collision.normalX();
        double normalY = collision.normalY();

        if (normalX == 0.0 && normalY == 0.0) {
            ballVelocityY = -ballVelocityY;
            return;
        }

        double dotProduct = ballVelocityX * normalX + ballVelocityY * normalY;
        ballVelocityX = ballVelocityX - 2.0 * dotProduct * normalX;
        ballVelocityY = ballVelocityY - 2.0 * dotProduct * normalY;
    }

    private void stopBall() {
        ballVelocityX = 0.0;
        ballVelocityY = 0.0;
    }

    private void render() {
        StdDraw.clear(BACKGROUND);
        drawBricks();
        drawBall();
        drawPaddle();
        drawHud();
        drawStateMessage();
    }

    private void drawBricks() {
        for (int i = 0; i < BRICK_COORDINATES.length; i++) {
            if (!bricksAlive[i]) {
                continue;
            }
            StdDraw.setPenColor(BRICK_PALETTE[i % BRICK_PALETTE.length]);
            StdDraw.filledRectangle(BRICK_COORDINATES[i][0], BRICK_COORDINATES[i][1], BRICK_HALF_WIDTH, BRICK_HALF_HEIGHT);
        }
    }

    private void drawBall() {
        StdDraw.setPenColor(BALL_COLOR);
        StdDraw.filledCircle(ballX, ballY, BALL_RADIUS);
    }

    private void drawPaddle() {
        StdDraw.setPenColor(PADDLE_COLOR);
        StdDraw.filledRectangle(paddleX, PADDLE_Y, PADDLE_HALF_WIDTH, PADDLE_HALF_HEIGHT);
    }

    private void drawHud() {
        StdDraw.setPenColor(TEXT_COLOR);
        StdDraw.setFont(SCORE_FONT);
        StdDraw.text(CANVAS_WIDTH - 95, CANVAS_HEIGHT - 35, "Score: " + score);

        StdDraw.setFont(HUD_FONT);
        if (!launched) {
            StdDraw.text(92, CANVAS_HEIGHT - 35, String.format("Angle: %.0f", angle));
            drawAimArrow();
        } else if (paused) {
            StdDraw.text(75, CANVAS_HEIGHT - 35, "Paused");
        }
    }

    private void drawAimArrow() {
        double arrowEndX = ballX + AIM_ARROW_LENGTH * Math.cos(Math.toRadians(angle));
        double arrowEndY = ballY + AIM_ARROW_LENGTH * Math.sin(Math.toRadians(angle));
        StdDraw.setPenColor(AIM_COLOR);
        StdDraw.setPenRadius(0.008);
        StdDraw.line(ballX, ballY, arrowEndX, arrowEndY);
        StdDraw.setPenRadius();
    }

    private void drawStateMessage() {
        if (!gameOver && !victory) {
            return;
        }

        StdDraw.setPenColor(TEXT_COLOR);
        StdDraw.setFont(STATUS_FONT);
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 - 40.0, victory ? "VICTORY" : "GAME OVER");
        StdDraw.setFont(HUD_FONT);
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 - 82.0, "Press Space to restart");
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private record Collision(boolean hit, double normalX, double normalY) {
        private static Collision none() {
            return new Collision(false, 0.0, 0.0);
        }
    }
}
