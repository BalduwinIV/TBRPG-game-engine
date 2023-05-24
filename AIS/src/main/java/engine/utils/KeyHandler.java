package engine.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for getting and analyzing keyboard input events.
 */
public class KeyHandler implements KeyListener {
    private int pressedKey;
    private final AtomicBoolean isPressed;
    private int typedKey;
    private final AtomicBoolean enableTyping;

    public KeyHandler () {
        pressedKey = 0;
        isPressed = new AtomicBoolean(false);
        typedKey = 0;
        enableTyping = new AtomicBoolean(false);
    }

    /**
     * Makes handler to listen/not to listen characters, typed by keyboard.
     * Does not affect listening pressed keys.
     * @param   state   true to enable listening typed characters, false to disable.
     */
    public void switchTyping(boolean state) {
        enableTyping.set(state);
    }

    /**
     * If listening is enabled remembers typed character.
     * @param   e   the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (enableTyping.get()) {
            typedKey = e.getKeyCode();
        }
    }

    public int getTypedKey() {
        return typedKey;
    }

    public boolean isPressed() {
        return isPressed.get();
    }

    /**
     * Remembers key that is being pressed to buffer.
     * @param   e   the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!isPressed.get()) {
            isPressed.set(true);
        }
        pressedKey = e.getKeyCode();
    }

    public int getPressedKey() {
        return pressedKey;
    }

    /**
     * Removes key that has been released from buffer.
     * @param   e   the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        isPressed.set(false);
    }
}
