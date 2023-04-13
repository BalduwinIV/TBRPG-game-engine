package engine.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for getting and analyzing keyboard input events.
 */
public class KeyHandler implements KeyListener {
    private List<Integer> pressedKeys;
    private List<Character> typedKeys;
    private AtomicBoolean enableTyping;

    public KeyHandler () {
        pressedKeys = new ArrayList<Integer>(5);
        typedKeys = new ArrayList<Character>();
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
            typedKeys.add(e.getKeyChar());
        }
    }

    /**
     * Clears typed characters buffer.
     */
    public void freeTypedKeysList() {
        typedKeys.clear();
    }

    /**
     * Remembers key that is being pressed to buffer.
     * @param   e   the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    /**
     * Returns list of keys, that is being pressed.
     * @return  Currently pressed keys list.
     */
    public List<Integer> getPressedKeys() {
        return pressedKeys;
    }

    /**
     * Removes key that has been released from buffer.
     * @param   e   the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int remove_index = pressedKeys.indexOf(e.getKeyCode());
        if (remove_index >= 0) {
            pressedKeys.remove((remove_index));
        }
    }
}
