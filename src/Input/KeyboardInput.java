package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
    private static KeyboardInput instance;

    private final boolean []keyCodes;
    private boolean keyW;
    private boolean keyA;
    private boolean keyS;
    private boolean keyD;
    private boolean space;

    private KeyboardInput(){
        keyCodes = new boolean[256];
        keyW = false;
        keyA = false;
        keyS = false;
        keyD = false;
        space = false;
    }

    public static KeyboardInput getInstance() {
        if(instance == null){
            instance = new KeyboardInput();
        }
        return instance;
    }

    public void updateInputKey(){
        keyW = keyCodes[KeyEvent.VK_W];
        keyA = keyCodes[KeyEvent.VK_A];
        keyS = keyCodes[KeyEvent.VK_S];
        keyD = keyCodes[KeyEvent.VK_D];
        space = keyCodes[KeyEvent.VK_SPACE];
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

    public boolean getKeyW(){
        return keyW;
    }
    public boolean getKeyA(){
        return keyA;
    }
    public boolean getKeyS(){
        return keyS;
    }
    public boolean getKeyD(){
        return keyD;
    }


}
