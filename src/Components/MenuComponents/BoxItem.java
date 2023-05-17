package Components.MenuComponents;

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
    private final Rectangle collideBox;

    private boolean previousClicked = false;
    private ColorType backgroundColor = ColorType.DEFAULT_BACKGROUND;
    public BoxItem(Rectangle collideBox){
        this.collideBox = collideBox;
    }
    @Override
    public void update() {
        if (collideBox.contains(MouseInput.get().getPosition())) {
            backgroundColor = ColorType.HOVER_BACKGROUND;
            if (MouseInput.get().isLeftMousePressed()) {
                if (!previousClicked) {
                    previousClicked = true;
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
}
