package engine.utils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for working with mouse input events.
 */
public class MouseHandler extends MouseAdapter implements MouseListener {
    private final JPanel parent;
    private final MouseMotionHandler mouseMotionHandler;
    private final AtomicBoolean isClicked;
    private int[] clickedPosition;
    private int clickedButton;
    private final AtomicBoolean isDoubleClicked;
    private int[] doubleClickedPosition;
    private int doubleClickedButton;
    private final AtomicBoolean isPressed;
    private int pressedButton;
    private final AtomicBoolean isOnScreen;

    public MouseHandler(JPanel parent, MouseMotionHandler mouseMotionHandler) {
        this.parent = parent;
        this.mouseMotionHandler = mouseMotionHandler;
        clickedPosition = new int[2];
        doubleClickedPosition = new int[2];
        isPressed = new AtomicBoolean(false);
        isOnScreen = new AtomicBoolean(true);
        isClicked = new AtomicBoolean(false);
        isDoubleClicked = new AtomicBoolean(false);
    }

    public boolean isClicked() {
        return isClicked.get();
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked.set(isClicked);
    }

    public boolean isDoubleClicked() {
        return isDoubleClicked.get();
    }

    public void setIsDoubleClicked(boolean isDoubleClicked) {
        this.isDoubleClicked.set(isDoubleClicked);
    }

    public int[] getClickedLocation() {
        return clickedPosition;
    }

    public int getClickedButton() {
        return clickedButton;
    }

    public int[] getDoubleClickedLocation() {
        return doubleClickedPosition;
    }

    public int getDoubleClickedButton() {
        return doubleClickedButton;
    }

    public int getPressedButton() {
        return pressedButton;
    }

    /**
     * Returns cursors position as array [x, y].
     * @return  Cursor position as array [x, y].
     */
    public int[] getCursorPosition() {
        return mouseMotionHandler.getCursorPosition();
    }


    public boolean isPressed() {
        return isPressed.get();
    }

    public boolean isOnScreen() {
        return isOnScreen.get();
    }

    /**
     * Remembers (double) click position in a list.
     * @param   e   the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            isDoubleClicked.set(true);
            doubleClickedPosition = mouseMotionHandler.getCursorPosition();
            doubleClickedButton = e.getButton();
        } else {
            isClicked.set(true);
            clickedPosition = mouseMotionHandler.getCursorPosition();
            clickedButton = e.getButton();
        }
    }

    /**
     * Sets pressed state as true.
     * @param   e   the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        isPressed.set(true);
        pressedButton = e.getButton();
    }

    /**
     * Sets pressed state as false.
     * @param   e   the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed.set(false);
    }

    /**
     * Sets onScreen state as true.
     * @param   e   the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        parent.grabFocus();
        isOnScreen.set(true);
    }

    /**
     * Sets onScreen state as false.
     * @param   e   the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        isOnScreen.set(false);
    }
}
