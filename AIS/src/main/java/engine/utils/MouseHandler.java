package engine.utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for working with mouse input events.
 */
public class MouseHandler implements MouseListener {
    private final List<Point> clickedList;
    private final List<Point> doubleClickedList;
    private final AtomicBoolean isPressed;
    private final AtomicBoolean isOnScreen;
    private final int[] cursorPosition;
    private final int[] lastCursorPosition;

    public MouseHandler() {
        clickedList = new ArrayList<>(1);
        doubleClickedList = new ArrayList<>(1);
        isPressed = new AtomicBoolean(false);
        isOnScreen = new AtomicBoolean(true);
        cursorPosition = new int[2];
        lastCursorPosition = new int[2];
    }

    /**
     * Frees last clicks positions from a list.
     */
    public void freeClickedList() {
        clickedList.clear();
    }

    /**
     * Frees last double clicks positions from a list.
     */
    public void freeDoubleClickedList() {
        doubleClickedList.clear();
    }

    /**
     * Returns cursors position as array [x, y].
     * @return  Cursor position as array [x, y].
     */
    public int[] getCursorPosition() {
        lastCursorPosition[0] = cursorPosition[0];
        lastCursorPosition[1] = cursorPosition[1];
        Point pointerPosition = MouseInfo.getPointerInfo().getLocation();
        cursorPosition[0] = pointerPosition.x;
        cursorPosition[1] = pointerPosition.y;

        return cursorPosition;
    }

    public int[] getLastCursorPosition() {
        return lastCursorPosition;
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
            doubleClickedList.add(e.getLocationOnScreen());
        } else {
            clickedList.add(e.getLocationOnScreen());
        }
    }

    /**
     * Sets pressed state as true.
     * @param   e   the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        lastCursorPosition[0] = cursorPosition[0];
        lastCursorPosition[1] = cursorPosition[0];
        isPressed.set(true);
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
