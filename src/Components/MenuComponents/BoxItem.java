package Components.MenuComponents;

import Components.Notifiable;
import Components.StaticComponent;
import Database.Database;
import Enums.ColorType;
import Enums.ComponentType;
import Enums.MessageType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Utils.Rectangle;

import java.awt.*;

/**
 * This class encapsulates the behavior of a box within an inventory.
 *
 * @see StaticComponent
 */
public class BoxItem implements StaticComponent {
    /**
     * Reference to the component that must be notified.
     */
    private final Notifiable inventory;

    /**
     * Intersection box of the button.
     */
    private final Rectangle collideBox;

    /**
     * Variable that stores the color of the button.
     */
    private ColorType backgroundColor = ColorType.DEFAULT_BACKGROUND;
    /**
     * Variable that keeps the evidence that a click was made at a certain moment of time.
     */
    private boolean previousClicked = false;

    /**
     * Variable specific to class that gives identifiers to instances.
     */
    private static int boxCounter = 0;

    /**
     * Variable that stores the current box id
     */
    private final int boxId;

    /**
     * This constructor initializes the basic components of the button.
     *
     * @param inventory  reference to the component that must be notified
     * @param collideBox intersection box of the component.
     */
    public BoxItem(Notifiable inventory, Rectangle collideBox) {
        boxId = boxCounter++;
        this.inventory = inventory;
        this.collideBox = collideBox;
    }

    @Override
    public void update() {
        if (collideBox.contains(MouseInput.get().getPosition())) {
            backgroundColor = ColorType.HOVER_BACKGROUND;
            if (MouseInput.get().isLeftMousePressed()) {
                if (!previousClicked) {
                    previousClicked = true;
                    inventory.notify(new Message(MessageType.BUTTON_CLICKED, ComponentType.BOX_INFO, boxId));
                }
            } else {
                previousClicked = false;
            }
        } else {
            backgroundColor = ColorType.DEFAULT_BACKGROUND;
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(backgroundColor.getColor());
        graphics2D.fillRect(collideBox.getMinX(), collideBox.getMinY(), collideBox.getWidth(), collideBox.getHeight());
    }

    /**
     * Getter for the box rectangle.
     *
     * @return collide box
     */
    public Rectangle getRectangle() {
        return collideBox;
    }
}
