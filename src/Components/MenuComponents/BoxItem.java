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

public class BoxItem implements StaticComponent {
    private static int boxCounter = 0;
    private int boxId ;
    private final Notifiable inventory;
    private final Rectangle collideBox;
    private boolean previousClicked = false;
    private boolean isSelected = false;
    private ColorType backgroundColor = ColorType.DEFAULT_BACKGROUND;
    public BoxItem(Notifiable inventory , Rectangle collideBox){
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
        }else {
            backgroundColor = ColorType.DEFAULT_BACKGROUND;
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(backgroundColor.getColor());
        graphics2D.fillRect(collideBox.getMinX(),collideBox.getMinY(), collideBox.getWidth(), collideBox.getHeight());
    }

    public Rectangle getRectangle(){
        return collideBox;
    }

    public void setColor(ColorType colorType) {
        backgroundColor = colorType;
    }
}
