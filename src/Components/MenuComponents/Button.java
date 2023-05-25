package Components.MenuComponents;

import Components.Notifiable;
import Components.StaticComponent;
import Database.Database;
import Enums.ColorType;
import Enums.ComponentType;
import Enums.MessageType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.util.Objects;

/**
 * This class encapsulates the behavior of a button.
 *
 * @see StaticComponent
 */
public class Button implements StaticComponent {
    /**
     * Reference to the component that must be notified.
     */
    private final Notifiable scene;

    /**
     * Intersection box of the button.
     */
    private final Rectangle collideBox;

    /**
     * Content of the button.
     */
    private final Text text;

    /**
     * Variable that stores the type of the button.
     */
    private final ComponentType currentType;

    /**
     * Variable that stores the color of the button.
     */
    private ColorType backgroundColor = ColorType.DEFAULT_BACKGROUND;

    /**
     * Variable that keeps the evidence that a click was made at a certain moment of time.
     */
    private boolean previousClicked = false;

    /**
     * This constructor initializes the basic components of the button.
     * @param scene reference to the component that must be notified.
     * @param type button identifier
     * @param info text content of the button
     * @param collideBox intersection box of the button.
     * @param size size of the text
     */
    public Button(Notifiable scene, ComponentType type, String info, Rectangle collideBox, int size) {
        this.scene = scene;
        this.currentType = type;
        this.collideBox = collideBox;
        this.text = new Text(info, new Coordinate<>(collideBox.getCenterX(), collideBox.getCenterY()), size);
    }

    @Override
    public void update() {
        if (collideBox.contains(MouseInput.get().getPosition())) {
            backgroundColor = ColorType.HOVER_BACKGROUND;
            text.setTextColor(ColorType.HOVER_TEXT);
            if (MouseInput.get().isLeftMousePressed()) {
                if (!previousClicked) {
                    if (currentType == ComponentType.SAVE_INFO) {
                        Database.get().setSaveToBeLoaded(text.getText().split(" ")[0]);
                    }
                    scene.notify(new Message(MessageType.BUTTON_CLICKED, currentType, -1));
                    previousClicked = true;
                }
            } else {
                previousClicked = false;

            }
        } else {
            if (!Objects.equals(Database.get().getSaveToBeLoaded(), text.getText().split(" ")[0])) {
                backgroundColor = ColorType.DEFAULT_BACKGROUND;
                text.setTextColor(ColorType.DEFAULT_TEXT);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(backgroundColor.getColor());
        graphics2D.fillRect(collideBox.getMinX(), collideBox.getMinY(), collideBox.getWidth(), collideBox.getHeight());
        text.draw(graphics2D);
    }

    /**
     * Getter for the content of the button.
     * @return text component
     */
    public Text getText() {
        return text;
    }
}
