package edu.macalester.comp124.breakout;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

import java.awt.event.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks input and allows for access to mouse and keyboard state at any time.
 * Created by Roland on 10/22/2015.
 */
public class InputManager implements MouseListener, MouseMotionListener, KeyListener {

    private boolean mouseDown;
    private GPoint mousePos;
    private ConcurrentHashMap<Integer, Boolean> keyStates;

    public boolean isKeyDown(int keyCode) {
        if(keyStates.containsKey(keyCode)) {
            return keyStates.get(keyCode);
        }
        return false;
    }
    public boolean isMouseDown() { return mouseDown; }
    public GPoint getMousePosition() { return mousePos; }
    public double getMouseX() { return mousePos.getX(); }
    public double getMouseY() { return mousePos.getY(); }
    public void setMousePos(double xPos, double yPos) { this.mousePos.setLocation(xPos, yPos);}

    /**
     * Constructs an InputManager that will listen to input on the given GraphicsProgram
     * @param parentProgram the program to listen to input on
     */
    public InputManager(GraphicsProgram parentProgram) {
        parentProgram.addMouseListeners(this);
        parentProgram.addKeyListeners(this);
        keyStates = new ConcurrentHashMap<Integer, Boolean>();
        mousePos = new GPoint(Double.NaN, Double.NaN);
        mouseDown = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyStates.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyStates.put(e.getKeyCode(), false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos = new GPoint(e.getPoint());
    }
}
