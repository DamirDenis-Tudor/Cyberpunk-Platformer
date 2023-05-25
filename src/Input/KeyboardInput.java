package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class register different keyboard events.
 */
public class KeyboardInput implements KeyListener {
    private static KeyboardInput instance;
    private final boolean[] keyCodes;
    private boolean esc;
    private boolean keyW;
    private boolean keyA;
    private boolean keyS;
    private boolean keyD;
    private boolean keyEnter;
    private boolean keyShift;
    private boolean keyDelete;
    private boolean previousKeyDelete;
    private boolean space;
    private boolean previousKeyW;
    private boolean previousSpace;


    private KeyboardInput() {
        keyCodes = new boolean[256];
        keyW = false;
        keyA = false;
        keyS = false;
        keyD = false;
        esc = false;
        keyEnter = false;
        keyShift = false;
        space = false;
        previousSpace = false;
        keyDelete = false;
        previousKeyDelete = false;
    }

    /**
     * @return shared instance
     */
    public static KeyboardInput get() {
        if (instance == null) {
            instance = new KeyboardInput();
        }
        return instance;
    }

    public void updateInputKey() {
        previousSpace = space;
        previousKeyW = keyW;
        previousKeyDelete = keyDelete;
        keyW = keyCodes[KeyEvent.VK_W];
        keyA = keyCodes[KeyEvent.VK_A];
        keyS = keyCodes[KeyEvent.VK_S];
        keyD = keyCodes[KeyEvent.VK_D];
        keyEnter = keyCodes[KeyEvent.VK_ENTER];
        keyShift = keyCodes[KeyEvent.VK_SHIFT];
        space = keyCodes[KeyEvent.VK_SPACE];
        esc = keyCodes[KeyEvent.VK_ESCAPE];
        keyDelete = keyCodes[KeyEvent.VK_DELETE];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyCodes[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyCodes[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public boolean getKeyA() {
        return keyA;
    }

    public boolean getKeyS() {
        return keyS;
    }

    public boolean getKeyD() {
        return keyD;
    }

    public boolean getKeyShift() {
        return keyShift;
    }

    public boolean getKeyEnter() {
        return keyEnter;
    }

    public boolean getKeyDelete() {return keyDelete;}
    public boolean getPreviousKeyDelete() {
        return previousKeyDelete;
    }

    public boolean getSpace() {
        return space;
    }

    public boolean getPreviousKeyW() {
        return previousKeyW;
    }

    public boolean getPreviousSpace() {
        return previousSpace;
    }

    public boolean isEsc() {
        return esc;
    }


    public void reset() {
        previousSpace = false;
        previousKeyW = false;
        keyW = false;
        keyA = false;
        keyS = false;
        keyD = false;
        keyEnter = false;
        keyShift = false;
        space = false;
        esc = false;
        keyDelete = false;
        previousKeyDelete = false;
    }
}
