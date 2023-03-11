package Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 */
public class MouseInput implements MouseListener {
    private static MouseInput instance;

    private MouseInput(){

    }

    public static MouseInput getInstance(){
        if (instance == null) {
            instance = new MouseInput();
        }
        return instance;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
