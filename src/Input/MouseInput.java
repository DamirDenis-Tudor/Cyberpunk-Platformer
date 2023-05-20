package Input;

import Utils.Coordinate;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This class handle mouse events.
 */
public class MouseInput implements MouseListener, MouseMotionListener {
    private static MouseInput instance;
    private final Coordinate<Integer> position;
    private boolean isLeftMousePreviousPressed;
    private boolean isRightMousePreviousPressed;
    private boolean isLeftMousePressed;
    private boolean isRightMousePressed;

    private MouseInput(){
        position = new Coordinate<>(0,0);
        isRightMousePressed = false;
        isRightMousePreviousPressed = false;
        isLeftMousePreviousPressed= false;
        isLeftMousePressed = false;
    }

    public static MouseInput get(){
        if (instance == null) {
            instance = new MouseInput();
        }
        return instance;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

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
        isLeftMousePreviousPressed = isLeftMousePressed;
        isRightMousePreviousPressed = isRightMousePressed;
        isRightMousePressed = false;
        isLeftMousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        position.setX(e.getX());
        position.setY(e.getY());
    }

    public boolean isLeftMousePressed() {
        return isLeftMousePressed;
    }

    public boolean isLeftMousePreviousPressed() {
        return isLeftMousePreviousPressed;
    }

    public boolean isRightMousePressed() {
        return isRightMousePressed;
    }

    public boolean isRightMousePreviousPressed() {
        return isRightMousePreviousPressed;
    }

    public Coordinate<Integer> getPosition() {
        return position;
    }

    public void reset(){
        isLeftMousePressed = false;
        isRightMousePressed = false;
    }
}
