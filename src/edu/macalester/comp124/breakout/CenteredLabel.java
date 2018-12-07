package edu.macalester.comp124.breakout;

import acm.graphics.GLabel;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

import java.awt.*;

/**
 * Created by Roland on 10/27/2015.
 */
public class CenteredLabel {

    private GLabel label;
    private GPoint windowCenter;

    public CenteredLabel(GraphicsProgram graphicsProgram, int windowWidth, int windowHeight, int fontSize) {
        label = new GLabel("");
        graphicsProgram.add(label);
        windowCenter = new GPoint(windowWidth / 2, windowHeight / 2);
        setFontSize(fontSize);
        hide();
    }

    /**
     * Sets the text of the label
     * @param str the text to display
     */
    public void setLabel(String str) {
        label.setLabel(str);
        label.setLocation(new GPoint(windowCenter.getX() - (label.getWidth() / 2), windowCenter.getY() - (label.getHeight() / 2)));
    }

    /**
     * Sets the color of this label's text
     * @param c a color
     */
    public void setColor(Color c) {
        label.setColor(c);
    }

    /**
     * Sets the size of the font
     * @param fontSize the size of the font
     */
    public void setFontSize(int fontSize) {
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
    }
    /**
     * Shows the label
     */
    public void show() {
        label.sendToFront();
        label.setVisible(true);
    }

    /**
     * Shows the given colored text
     * @param text the text to display
     * @param c the color of the displayed text
     */
    public void show(String text, Color c) {
        setLabel(text);
        setColor(c);
        label.setVisible(true);
        label.sendToFront();
    }

    /**
     * Hides the label
     */
    public void hide() {
        label.setVisible(false);
    }
}
