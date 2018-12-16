package edu.macalester.comp124.breakout;

import acm.graphics.*;
import acm.program.GraphicsProgram;

import java.util.Collection;

/**
 * Represents a ball
 * Created by rmunsil on 10/19/15.
 */
public class Ball extends GOval {



    private GPoint prevTopLeft;
    public GPoint getPrevTopLeft()   { return prevTopLeft; }
    public void setPrevTopLeft(GPoint prevTopLeft)   { this.prevTopLeft = prevTopLeft;}

    private double velX;
    public double getVelX()          { return velX; }
    public void setVelX(double velX) { this.velX = velX; }
    public void reverseVelX()        { velX = -velX; }

    private double velY;
    public double getVelY()          { return velY; }
    public void setVelY(double velY) { this.velY = velY; }
    public void reverseVelY()        { velY = -velY; }

    public void setVelocity(double x, double y){ this.velX = x; this.velY = y; }
    public double getRadius() { return  this.getWidth() / 2; }
    public double getRightX() { return getX() + getWidth(); }
    public double getBottomY() { return getY() + getHeight(); }

    /**
     * Constructs a ball object
     * @param initialTopLeft the initial position of the upper-left corner of the ball
     * @param ballDiameter the diameter of the ball
     */
    public Ball(GPoint initialTopLeft, double ballDiameter) {
        super(initialTopLeft.getX(), initialTopLeft.getY(), ballDiameter, ballDiameter);
        velX = 0;
        velY = 0;
    }

    /**
     * Applies the velocity to this object's position.
     */
    public void applyVelocity(){
        prevTopLeft = this.getLocation();
        move(velX, velY);
    }

    /**
     * Determines whether this ball intersects a given rectangle
     * @param rect a rectangle
     * @return whether it intersects the rectangle
     */
    public boolean intersects(GRect rect) {
        return intersectsWhenAtSpecificPosition(rect, this.getLocation());
    }

    /**
     * Determines whether this ball intersects a given rectangle when the ball is at a specific position
     * @param rect a rectangle
     * @param fakePos the position of the ball
     * @return whether it intersects the rectangle
     */
    private boolean intersectsWhenAtSpecificPosition(GRect rect, GPoint fakePos) {
        //See http://stackoverflow.com/a/1879223

        double rectHalfWidth = rect.getWidth() / 2;
        double rectHalfHeight = rect.getHeight() / 2;
        double ballCenterX = fakePos.getX() + getRadius();
        double ballCenterY = fakePos.getY() + getRadius();
        double rectCenterX = rect.getX() + rectHalfWidth;
        double rectCenterY = rect.getY() + rectHalfHeight;


        //Minimum distance between their centers along the x-axis.
        double minDistX = getRadius() + rectHalfWidth;
        //Minimum distance between their centers along the y-axis.
        double minDistY = getRadius() + rectHalfHeight;

        //Actual distance between their centers along the x-axis.
        double distX = Math.abs(ballCenterX - rectCenterX);
        //Actual distance between their centers along the y-axis.
        double distY = Math.abs(ballCenterY - rectCenterY);

        //They are too far away along the x-axis to collide.
        if(distX > minDistX) return false;
        //They are too far away along the y-axis to collide.
        if(distY > minDistY) return false;

        //They are close enough that they must collide.
        //Note that this is only true because we know from the previous
        //two tests that they are closer than the minDists
        if(distX <= rectHalfWidth) return true;
        if(distY <= rectHalfHeight) return true;

        //Check if the circle intersects the corner points.
        double distFromCorner = GMath.distance(distX - rectHalfWidth, distY - rectHalfHeight);
        return distFromCorner <= getRadius();
    }

    /**
     * Adjusts this ball's velocity based on how it's collided with the rectangle
     * @param rect a rectangle
     */
    public void handleCollision(GRect rect) {

        //Do a binary search for the exact point that the ball intersected at
        double t = 0.5;
        double delta = 0.25;
        GPoint start = prevTopLeft;
        GPoint end = getLocation();

        GPoint testPoint = new GPoint(Double.NaN, Double.NaN);
        while(delta > 0.01) {
            testPoint = lerp(start, end, t);
            boolean intersects = intersectsWhenAtSpecificPosition(rect, testPoint);
            t = intersects ? t - delta : t + delta;
            delta /= 2;
        }
        //Now testPoint is the position of the ball right when it hit the brick.

        //The center of the ball when it hit the brick
        GPoint centerAtHit = new GPoint(testPoint.getX() + getRadius(), testPoint.getY() + getRadius());

        //If the ball's x pos is between the rectangle's min & max x, bounce vertically
        if(centerAtHit.getX() >= rect.getX() && centerAtHit.getX() <= rect.getX() + rect.getWidth()) {
            reverseVelY();
            return;
        }

        //If the ball's y pos is between the rectangle's min & max y, bounce horizontally
        if(centerAtHit.getY() >= rect.getY() && centerAtHit.getY() <= rect.getY() + rect.getHeight()) {
            reverseVelX();
            return;
        }

        //Otherwise the ball must have hit a corner first. Just reverse the ball's direction
        reverseVelX();
        reverseVelY();
    }

    /**
     * Linear interpolate between two point
     * @param p1 Point 1
     * @param p2 Point 2
     * @param amount between 0 and 1
     * @return interpolated point
     */
    private GPoint lerp(GPoint p1, GPoint p2, double amount) {
        return new GPoint(p1.getX() + ((p2.getX() - p1.getX()) * amount), p1.getY() + ((p2.getY() - p1.getY()) * amount));
    }


}
