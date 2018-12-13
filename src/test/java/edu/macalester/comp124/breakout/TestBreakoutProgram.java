package edu.macalester.comp124.breakout;

import acm.graphics.GObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
public class TestBreakoutProgram {

    @Mock
    Ball mockBall;

    @Mock
    Paddle mockPaddle;

    @Mock
    ArrayList<Brick> mockBricks;

    @Mock
    Brick mockSingleBrick;

    @Mock
    InputManager mockInputManager;

    // Object under test
    @Spy
    BreakoutProgram breakoutProgram = new BreakoutProgram();

    double MOCK_PADDLE_WIDTH = 4;
    double MOCK_PADDLE_Y_POS = 10;

    @Before
    public void createObjectAndInjectMocks() {
        breakoutProgram.ball = mockBall;
        breakoutProgram.paddle = mockPaddle;
        breakoutProgram.bricks = mockBricks;
        breakoutProgram.inputManager = mockInputManager;

        // program will not be properly initialized so we can't actually call remove
        Mockito.doNothing().when(breakoutProgram).remove(any(GObject.class));
    }

    private void prepareForBrickCollisionTests() {
        when(mockBall.getVelX()).thenReturn(0.0);
        when(mockBall.getVelY()).thenReturn(0.0);
        when(mockBall.getRightX()).thenReturn(20.0);
        when(mockBall.getX()).thenReturn(10.0);
        when(mockBall.getY()).thenReturn(10.0);

        when(mockBall.intersects(mockPaddle)).thenReturn(false);
    }

    @Test
    public void brickIsRemovedIfBallIntersectsIt() {
        prepareForBrickCollisionTests();

        when(mockBricks.size()).thenReturn(1);
        when(mockBricks.get(any(int.class))).thenReturn(mockSingleBrick);
        when(mockBall.intersects(mockSingleBrick)).thenReturn(true);

        breakoutProgram.doOneStep();

        verify(mockBricks).remove(mockSingleBrick);
        verify(breakoutProgram).remove(mockSingleBrick);
    }

    @Test
    public void onlyOneBrickCanBeRemovedPerFrame() {
        prepareForBrickCollisionTests();

        when(mockBricks.size()).thenReturn(10);
        when(mockBricks.get(any(int.class))).thenReturn(mockSingleBrick);
        when(mockBall.intersects(mockSingleBrick)).thenReturn(true);

        breakoutProgram.doOneStep();

        verify(mockBricks, times(1)).remove(mockSingleBrick);
        verify(breakoutProgram, times(1)).remove(mockSingleBrick);
    }

    @Test
    public void paddleMovesWithMouse() {

        when(mockPaddle.getWidth()).thenReturn(MOCK_PADDLE_WIDTH);
        when(mockPaddle.getY()).thenReturn(MOCK_PADDLE_Y_POS);


        double fakeXPos1 = breakoutProgram.WINDOW_WIDTH/2.0;
        when(mockInputManager.getMouseX()).thenReturn(fakeXPos1);

        breakoutProgram.roundInnerLoop();

        verify(mockPaddle).setLocation(fakeXPos1-MOCK_PADDLE_WIDTH/2, MOCK_PADDLE_Y_POS);


        double fakeXPos2 = breakoutProgram.WINDOW_WIDTH/3.0;
        when(mockInputManager.getMouseX()).thenReturn(fakeXPos2);

        breakoutProgram.roundInnerLoop();

        verify(mockPaddle).setLocation(fakeXPos2-MOCK_PADDLE_WIDTH/2, MOCK_PADDLE_Y_POS);
    }

    @Test
    public void paddleCannotGoPastLeftScreenBounds() {
        when(mockPaddle.getWidth()).thenReturn(MOCK_PADDLE_WIDTH);
        when(mockPaddle.getY()).thenReturn(MOCK_PADDLE_Y_POS);

        when(mockInputManager.getMouseX()).thenReturn(-1.0);

        breakoutProgram.roundInnerLoop();

        verify(mockPaddle).setLocation(geq(0.0), eq(MOCK_PADDLE_Y_POS));
    }

    @Test
    public void paddleCannotGoPastRightScreenBounds() {
        when(mockPaddle.getWidth()).thenReturn(MOCK_PADDLE_WIDTH);
        when(mockPaddle.getY()).thenReturn(MOCK_PADDLE_Y_POS);

        when(mockInputManager.getMouseX()).thenReturn(10000000.0);

        breakoutProgram.roundInnerLoop();

        verify(mockPaddle).setLocation(leq(breakoutProgram.WINDOW_WIDTH-MOCK_PADDLE_WIDTH), eq(MOCK_PADDLE_Y_POS));
    }

}
