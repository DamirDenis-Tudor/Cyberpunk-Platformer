package Input;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 */
public class MouseInput implements MouseListener {
    private static MouseInput instance;

    private boolean isLeftMousePressed;
    private boolean isRightMousePressed;
    private boolean isLeftMouseClicked;
    private boolean isRightMouseClicked;

    private MouseInput(){
        isLeftMouseClicked = false;
        isRightMousePressed = false;
        isRightMouseClicked = false;
        isLeftMousePressed = false;
    }

    public static MouseInput getInstance(){
        if (instance == null) {
            instance = new MouseInput();
        }
        return instance;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getModifiersEx()) {
            case InputEvent.BUTTON1_DOWN_MASK -> {
                // System.out.println("That's the LEFT button");
                isLeftMouseClicked = true;
            }
            case InputEvent.BUTTON2_DOWN_MASK -> {
                //System.out.println("That's the MIDDLE button");
            }
            case InputEvent.BUTTON3_DOWN_MASK -> {
                isRightMouseClicked = true;

            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getModifiersEx()) {
            case InputEvent.BUTTON1_DOWN_MASK -> {
                isLeftMousePressed = true;

            }
            case InputEvent.BUTTON2_DOWN_MASK -> {

            }
            case InputEvent.BUTTON3_DOWN_MASK -> {
                isRightMousePressed = true;

            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isLeftMouseClicked = false;
        isRightMousePressed = false;
        isRightMouseClicked = false;
        isLeftMousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
