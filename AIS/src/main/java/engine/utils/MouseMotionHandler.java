package engine.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class MouseMotionHandler implements MouseMotionListener {
    private final int[] cursorPosition;
    private final AtomicBoolean isBeingDragged;
    private final int[] cursorPositionDifference;

    public MouseMotionHandler() {
        this.cursorPosition = new int[2];
        this.isBeingDragged = new AtomicBoolean(false);
        this.cursorPositionDifference = new int[2];
    }

    public boolean isBeingDragged() {
        return isBeingDragged.get();
    }

    public int[] getCursorPosition() {
        return cursorPosition;
    }

    public int[] getCursorPositionDifference() {
        int[] cursorPositionDifferenceCopy = cursorPositionDifference.clone();
        cursorPosition[0] -= cursorPositionDifference[0];
        cursorPosition[1] -= cursorPositionDifference[1];
        cursorPositionDifference[0] = 0;
        cursorPositionDifference[1] = 0;

        return cursorPositionDifferenceCopy;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isBeingDragged.get()) {
            isBeingDragged.set(true);
        }
        cursorPositionDifference[0] = cursorPosition[0] - e.getX();
        cursorPositionDifference[1] = cursorPosition[1] - e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isBeingDragged.get()) {
            isBeingDragged.set(false);
        }
        cursorPosition[0] = e.getX();
        cursorPosition[1] = e.getY();
    }
}
