package edu.macalester.comp124.breakout;

import acm.graphics.GLabel;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;

import static org.mockito.Mockito.*;

import static org.mockito.Mockito.times;

@PrepareForTest({CenteredLabel.class, GLabel.class, Font.class})
@RunWith(PowerMockRunner.class)
public class TestCenteredLabel {

    @Mock
    GLabel mockGLabel;

    @Mock
    Font mockFont;

    @Mock
    GraphicsProgram mockGraphicsProgram;

    int EXPECTED_FONT_SIZE = 10;


    // Object being tested
    CenteredLabel centeredLabel;

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(GLabel.class).withAnyArguments().thenReturn(mockGLabel);
        PowerMockito.whenNew(Font.class).withArguments(any(String.class), any(int.class), eq(EXPECTED_FONT_SIZE)).thenReturn(mockFont);
    }

    private void constructCenteredLabel() {
        centeredLabel = new CenteredLabel(mockGraphicsProgram, 100, 100, EXPECTED_FONT_SIZE);
    }

    @Test
    public void constructorAddsLabelToGraphicsProgram() {
        constructCenteredLabel();
        verify(mockGraphicsProgram, times(1)).add(isA(GLabel.class));
    }

    @Test
    public void constructorSetsFontSizeAndHidesLabel() throws Exception {
        constructCenteredLabel();
        verify(mockGLabel).setFont(mockFont);
        verify(mockGLabel).setVisible(false);
    }

    @Test
    public void testShowWithStringAndColor() {
        constructCenteredLabel();

        String expectedText = "EXPECTED TEXT";
        Color expectedColor = Color.MAGENTA;

        centeredLabel.show(expectedText, expectedColor);

        verify(mockGLabel).setLabel(expectedText);

        //!!! NOTE: it's difficult to test this calculation with mocking because we would have to precalculate the text length
        verify(mockGLabel).setLocation(any(GPoint.class));
        verify(mockGLabel).setColor(expectedColor);
        verify(mockGLabel).setVisible(true);

        //NOTE: possible failure point if we figure out a way to, for example, ensure the label is always at the front
        verify(mockGLabel).sendToFront();
    }

}
