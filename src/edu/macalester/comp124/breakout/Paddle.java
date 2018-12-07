package edu.macalester.comp124.breakout;

import acm.graphics.GRect;

/**
 * Represents a paddle
 * Created by rmunsil on 10/20/15.
 */
public class Paddle extends GRect {

    /**
     * Constructs a paddle at a given location and of a given width and height
     * @param x the x position of the paddle's top left
     * @param y the y position of the paddle's top left
     * @param width the width of the paddle
     * @param height the height of the paddle
     */
    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
}
