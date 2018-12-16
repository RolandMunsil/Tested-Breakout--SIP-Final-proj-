package edu.macalester.comp124.breakout;

import acm.graphics.GLabel;
import acm.program.GraphicsProgram;
import java.awt.*;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestBreakoutProgram extends GraphicsProgram {

    BreakoutProgram breakoutProgram = new BreakoutProgram();

    @Test
    public void brickIsRemovedIfBallIntersectsIt() {
        breakoutProgram.init();
        int brickNumBeforeCollision = breakoutProgram.bricks.size();
        breakoutProgram.ball.setLocation(6, 100);
        breakoutProgram.ball.setPrevTopLeft(breakoutProgram.ball.getLocation()); //had to create setter for PrevTopLeft in Ball class
        breakoutProgram.doOneStep();
        int brickAfterCollision = breakoutProgram.bricks.size();
        assertEquals(brickNumBeforeCollision-1, brickAfterCollision);
    }

    @Test
    public void paddleMovesWithMouse() {
        breakoutProgram.init();
        breakoutProgram.inputManager.setMousePos(breakoutProgram.WINDOW_WIDTH/2.0, 100.0); //created setMousePos in InputManager class
        breakoutProgram.roundInnerLoop();
        assertEquals(breakoutProgram.inputManager.getMouseX() - breakoutProgram.paddle.getWidth()/2, breakoutProgram.paddle.getX(),0.1);
        assertEquals(breakoutProgram.WINDOW_HEIGHT - breakoutProgram.PADDLE_DISTANCE_FROM_BOTTOM, breakoutProgram.paddle.getY(), 0.1);

        breakoutProgram.inputManager.setMousePos(breakoutProgram.WINDOW_WIDTH/3.0, 250.0); //created setMousePos in InputManager class
        breakoutProgram.roundInnerLoop();
        assertEquals(breakoutProgram.inputManager.getMouseX() - breakoutProgram.paddle.getWidth()/2, breakoutProgram.paddle.getX(),0.1);
        assertEquals(breakoutProgram.WINDOW_HEIGHT - breakoutProgram.PADDLE_DISTANCE_FROM_BOTTOM, breakoutProgram.paddle.getY(), 0.1);
    }

    @Test
    public void paddleCannotGoPastLeftScreenBounds(){
        breakoutProgram.init();
        breakoutProgram.inputManager.setMousePos(-1.0, 100);
        breakoutProgram.roundInnerLoop();
        assertEquals(0.0, breakoutProgram.paddle.getX(),0.1);
        assertEquals(breakoutProgram.WINDOW_HEIGHT - breakoutProgram.PADDLE_DISTANCE_FROM_BOTTOM, breakoutProgram.paddle.getY(), 0.1);
    }

    @Test
    public void paddleCannotGoPastRightScreenBounds(){
        breakoutProgram.init();
        breakoutProgram.inputManager.setMousePos(10000000.0, 100);
        breakoutProgram.roundInnerLoop();
        assertEquals(breakoutProgram.WINDOW_WIDTH - breakoutProgram.paddle.getWidth(), breakoutProgram.paddle.getX(), 0.1);
    }

}