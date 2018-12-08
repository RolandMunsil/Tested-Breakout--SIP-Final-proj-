package edu.macalester.comp124.breakout;

import acm.graphics.GLabel;
import acm.program.GraphicsProgram;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import static org.mockito.Mockito.times;

public class TestCenteredLabel {

    @Test
    public void constructorAddsLabelToGraphicsProgram() {
        GraphicsProgram mockGraphicsProgram = mock(GraphicsProgram.class);
        CenteredLabel centeredLabel = new CenteredLabel(mockGraphicsProgram, 100, 100, 10);
        verify(mockGraphicsProgram, times(1)).add(isA(GLabel.class));
    }
}
