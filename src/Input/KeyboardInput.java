package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
    private static KeyboardInput instance;

    private KeyboardInput(){

    }

    public static KeyboardInput getInstance() {
        if(instance == null){
            instance = new KeyboardInput();
        }
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
