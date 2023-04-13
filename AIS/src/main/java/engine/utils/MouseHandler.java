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

    public MouseHandler() {
        clickedList = new ArrayList<>(1);
        doubleClickedList = new ArrayList<>(1);
        isPressed = new AtomicBoolean(false);
        isOnScreen = new AtomicBoolean(true);
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
        Point cursorPosition = MouseInfo.getPointerInfo().getLocation();
        int[] currentPosition = new int[2];
        currentPosition[0] = cursorPosition.x;
        currentPosition[1] = cursorPosition.y;

        return currentPosition;
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
