package edu.macalester.comp124.breakout;

import acm.graphics.*;
import acm.program.GraphicsProgram;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Main GraphicsProgram for the breakout game described
 * in exercise 10.10 int the Roberts Textbook.
 */
public class BreakoutProgram extends GraphicsProgram {

    Paddle paddle;
    Ball ball;
    ArrayList<Brick> bricks;
    InputManager inputManager;
    CenteredLabel centerText;

    final double BRICKS_SIDE_PADDING_X = 6;
    final double BRICKS_TOP_PADDING_Y = 100;
    final double BRICKS_BOTTOM_PADDING_Y = 500;
    final double NUM_BRICKS_X = 10;
    final double NUM_BRICKS_Y = 10;
    final double SPACE_BETWEEN_BRICKS_X = 3;
    final double SPACE_BETWEEN_BRICKS_Y = 3;
    final Color[] BRICK_COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};

    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 800;

    final long FRAME_LENGTH_MILLIS = 1000 / 60; //60fps

    final double BALL_Y_VELOCITY = 6;
    final double BALL_MAX_X_VELOCITY = 8;
    final double BALL_DIAMETER = 15;

    final double PADDLE_DISTANCE_FROM_BOTTOM = 50;
    final double PADDLE_WIDTH = 80;
    final double PADDLE_HEIGHT = 10;

    public void init() {
        setSizeFixed(WINDOW_WIDTH, WINDOW_HEIGHT);

        //Initialize InputManager
        inputManager = new InputManager(this);

        //Initialize center text
        centerText = new CenteredLabel(this, WINDOW_WIDTH, WINDOW_HEIGHT, 40);

        //Initialize paddle
        paddle = new Paddle(WINDOW_WIDTH / 2, WINDOW_HEIGHT - PADDLE_DISTANCE_FROM_BOTTOM, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setColor(new Color(0, 0, 0, 0));
        paddle.setFillColor(Color.RED);
        paddle.setFilled(true);
        add(paddle);

        //Initialize ball
        ball = new Ball(new GPoint(-10000, -10000), BALL_DIAMETER);
        ball.setVelocity(0, 0);
        ball.setFilled(true);
        ball.setFillColor(Color.BLACK);
        add(ball);

        //Generate bricks
        bricks = new ArrayList<Brick>();
        double brickWidth = (WINDOW_WIDTH - (BRICKS_SIDE_PADDING_X * 2) - (SPACE_BETWEEN_BRICKS_X * (NUM_BRICKS_X - 1))) / NUM_BRICKS_X;
        double brickHeight = (WINDOW_HEIGHT - (BRICKS_TOP_PADDING_Y + BRICKS_BOTTOM_PADDING_Y) - (SPACE_BETWEEN_BRICKS_Y * (NUM_BRICKS_Y - 1))) / NUM_BRICKS_Y;

        for (int row = 0; row < NUM_BRICKS_Y; row++) {
            for (int column = 0; column < NUM_BRICKS_X; column++) {
                double x = BRICKS_SIDE_PADDING_X + column * (brickWidth + SPACE_BETWEEN_BRICKS_X);
                double y = BRICKS_TOP_PADDING_Y + row * (brickHeight + SPACE_BETWEEN_BRICKS_Y);

                Brick brick = new Brick(new GPoint(x, y), brickWidth, brickHeight);
                brick.setFilled(true);
                brick.setColor(new Color(0, 0, 0, 0));
                brick.setFillColor(BRICK_COLORS[row / 2]);
                bricks.add(brick);
                add(brick);
            }
        }
    }

    /**
     * On Windows setSize doesn't work properly so this function adjusts for the error
     *
     * @param width  the desired width of the window
     * @param height the desired height of the window
     */
    private void setSizeFixed(int width, int height) {
        setSize(width, height);
        int diffW = width - getWidth();
        int diffH = height - getHeight();
        setSize(width + diffW, height + diffH);
    }

    public void run() {
        //On my Windows machines, the first setSizeFixed in init doesn't work.
        //So I call it again here
        setSizeFixed(WINDOW_WIDTH, WINDOW_HEIGHT);

        int lives = 3;

        while(true) {
            centerText.show("Click to start.", Color.BLACK);
            waitForInputThenStart();
            boolean hasWon;
            while (true) { //Do the game loop
                long startTime = System.currentTimeMillis();

                movePaddleWithMouse();

                //If the ball goes past the bottom of the screen, the player has lost
                if (ball.getY() > WINDOW_HEIGHT) {
                    hasWon = false;
                    break;
                }

                //If there are no more bricks, the player has won
                if(bricks.size() == 0) {
                    hasWon = true;
                    break;
                }

                doOneStep();

                long endTime = System.currentTimeMillis();
                pause(Math.max(0, FRAME_LENGTH_MILLIS - (endTime - startTime) - 1));
            }

            //Game loop is over, decide what to do based on whether or not player has won
            if(hasWon) {
                centerText.show("Congrats! You won!", new Color(0x77DF57));
                break;
            }
            else {
                lives--;
                if(lives == 0) {
                    centerText.show("You have run out of lives.", new Color(0xad5e59));
                    break;
                }
                else {
                    String livesString = lives == 1 ? "life" : "lives";
                    centerText.show("You have " + lives + " " + livesString + " left.", new Color(0xad5e59));
                    //Wait for input
                    while(!(inputManager.isKeyDown(KeyEvent.VK_SPACE) || inputManager.isMouseDown()));
                }
            }
        }
    }

    void doOneStep() {
        //Bounce off of screen bounds
        if (ball.getRightX() > WINDOW_WIDTH || ball.getX() < 0) {
            ball.reverseVelX();
        }
        if (ball.getY() < 0) {
            ball.reverseVelY();
        }

        //Handle collision with paddle
        if (ball.getVelY() > 0 && ball.intersects(paddle)) {
            ball.reverseVelY();

            double ballCenterX = ball.getX() + ball.getRadius();
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;

            //The ball's velocity will change more the further it is from the center of the paddle
            double factor = (ballCenterX - paddleCenterX) / 10;
            double newVelX = ball.getVelX() + factor;

            //Clamp to between -max x and max x
            newVelX = Math.min(BALL_MAX_X_VELOCITY, Math.max(newVelX, -BALL_MAX_X_VELOCITY));
            ball.setVelX(newVelX);
        }

        //Handle collision with bricks
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);

            if (ball.intersects(brick)) {
                ball.handleCollision(brick);
                //Remove it from the screen
                remove(brick);
                //Remove it from the ArrayList
                bricks.remove(brick);
                //Only allow the ball to collide with one brick per frame
                break;
            }
        }

        //Finally, move the ball
        ball.applyVelocity();
    }

    /**
     * Waits for the player to trigger start and then launches the ball
     */
    private void waitForInputThenStart() {

        while (true) {
            ball.setLocation(paddle.getX() + paddle.getWidth() / 2 - ball.getRadius(), paddle.getY() - ball.getHeight() - 10);
            movePaddleWithMouse();
            //I realize this could be put in the while statement, but it's easier to read when it's here
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE) || inputManager.isMouseDown()) {
                break;
            }
        }
        centerText.hide();

        //Use the ball's y velocity to calculate x velocity so that the
        //maximum angle that the ball can launch away at is 45deg
        ball.setVelocity(((Math.random() * 2) - 1) * BALL_Y_VELOCITY, -BALL_Y_VELOCITY);
    }

    /**
     * Sets the paddle's location based on the mouse's location
     */
    private void movePaddleWithMouse() {
        double proposedPaddleX = inputManager.getMouseX() - paddle.getWidth() / 2;
        if (proposedPaddleX < 0) {
            paddle.setLocation(0, paddle.getY());
        } else if (proposedPaddleX + paddle.getWidth() > WINDOW_WIDTH) {
            paddle.setLocation(WINDOW_WIDTH - paddle.getWidth(), paddle.getY());
        } else {
            paddle.setLocation(proposedPaddleX, paddle.getY());
        }
    }
}
