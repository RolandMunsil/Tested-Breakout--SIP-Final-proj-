package edu.macalester.comp124.breakout;

import acm.graphics.G3DRect;
import acm.graphics.GPoint;

/**
 * Represents a brick
 * Created by rmunsil on 10/20/15.
 */
public class Brick extends G3DRect {

    /**
     * Constructs a brick at a given location and of a given width and height
     * @param topLeft the location of the brick's top left
     * @param width the width of the brick
     * @param height the height of the brick
     */
    public Brick(GPoint topLeft, double width, double height) {
        super(topLeft.getX(), topLeft.getY(), width, height);
        this.setRaised(true);
    }
}
