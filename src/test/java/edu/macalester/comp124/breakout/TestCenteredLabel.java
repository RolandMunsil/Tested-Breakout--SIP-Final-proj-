package edu.macalester.comp124.breakout;

import acm.graphics.GLabel;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import java.awt.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;


public class TestCenteredLabel extends GraphicsProgram{
    GLabel testLabel = new GLabel("");
    GraphicsProgram graphicsProgram = new GraphicsProgram() {};
    CenteredLabel centeredLabel;
    int EXPECTED_FONT_SIZE = 10;

    private void constructCenteredLabel() {
        centeredLabel = new CenteredLabel(graphicsProgram, 100, 100, EXPECTED_FONT_SIZE);
    }

    @Test
    public void constructorAddsLabelToGraphicsProgram(){
        constructCenteredLabel();
        assertEquals(testLabel.getClass(), graphicsProgram.getElement(0).getClass());
    }

    @Test
    public void constructorSetsFontSize(){
        constructCenteredLabel();
    }

}