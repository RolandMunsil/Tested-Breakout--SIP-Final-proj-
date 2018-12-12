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

    // Object under test
    @Spy
    BreakoutProgram breakoutProgram = new BreakoutProgram();

    @Before
    public void createObjectAndInjectMocks() {
        breakoutProgram.ball = mockBall;
        breakoutProgram.paddle = mockPaddle;
        breakoutProgram.bricks = mockBricks;

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

}
