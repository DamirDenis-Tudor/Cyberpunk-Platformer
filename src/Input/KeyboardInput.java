package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * THis class register different keyboard events.
 */
public class KeyboardInput implements KeyListener {
    private static KeyboardInput instance;
    private final boolean[] keyCodes;

    private boolean esc;
    private boolean previousEsc;
    private boolean keyW;
    private boolean keyA;
    private boolean keyS;
    private boolean keyD;
    private boolean keyEnter;
    private boolean keyShift;
    private boolean space;
    private boolean previousKeyW;
    private boolean previousKeyA;
    private boolean previousKeyS;
    private boolean previousKeyD;
    private boolean previousKeyE;
    private boolean previousSpace;


    private KeyboardInput() {
        keyCodes = new boolean[256];
        keyW = false;
        keyA = false;
        keyS = false;
        keyD = false;
        esc = false;
        previousEsc = false;
        keyEnter = false;
        keyShift = false;
        space = false;
        previousSpace = false;
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
        previousKeyA = keyA;
        previousKeyW = keyW;
        previousKeyS = keyS;
        previousKeyD = keyD;
        previousKeyE = keyEnter;
        previousEsc = esc;
        keyW = keyCodes[KeyEvent.VK_W];
        keyA = keyCodes[KeyEvent.VK_A];
        keyS = keyCodes[KeyEvent.VK_S];
        keyD = keyCodes[KeyEvent.VK_D];
        keyEnter = keyCodes[KeyEvent.VK_ENTER];
        keyShift = keyCodes[KeyEvent.VK_SHIFT];
        space = keyCodes[KeyEvent.VK_SPACE];
        esc = keyCodes[KeyEvent.VK_ESCAPE];
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
    public boolean getKeyW() {
        return keyW;
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


    public boolean getSpace() {
        return space;
    }

    public boolean getPreviousKeyA() {
        return previousKeyA;
    }

    public boolean getPreviousKeyD() {
        return previousKeyD;
    }

    public boolean getPreviousKeyS() {
        return previousKeyS;
    }

    public boolean getPreviousKeyW() {
        return previousKeyW;
    }

    public boolean getPreviousSpace() {
        return previousSpace;
    }

    public boolean isPreviousKeyE() {
        return previousKeyE;
    }

    public boolean isEsc() {
        return esc;
    }

    public boolean isPreviousEsc() {
        return previousEsc;
    }
}
