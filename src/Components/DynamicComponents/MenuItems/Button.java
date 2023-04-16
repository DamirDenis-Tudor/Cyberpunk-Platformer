package Components.DynamicComponents.MenuItems;

import Components.StaticComponent;
import Enums.ColorType;
import Enums.ComponentType;
import Enums.MessageType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.GameWindow;

public class Button implements StaticComponent {
    private final Scene scene;
    private final ComponentType currentType;
    private ColorType backgroundColor = ColorType.DefaultBackground;
    private final Rectangle collideBox;
    private Text text;
    public Button(Scene scene, ComponentType type, Rectangle collideBox) {
        this.scene = scene;
        this.currentType = type;
        this.collideBox = collideBox;
        this.text = new Text(type, new Coordinate<>(collideBox.getCenterX() , collideBox.getCenterY()) , 56);
    }
    @Override
    public void update() throws Exception {
        if (collideBox.contains(MouseInput.getInstance().getPosition())) {
            backgroundColor = ColorType.HoverBackground;
            text.setTextColor(ColorType.HoverText);
            if (MouseInput.getInstance().isLeftMousePressed()) {
                scene.notify(new Message(MessageType.ButtonClicked, currentType, -1));
            }
        } else {
            backgroundColor = ColorType.DefaultBackground;
            text.setTextColor(ColorType.DefaultText);
        }
    }
    @Override
    public void draw() {
        GameWindow.getInstance().drawRectangle(collideBox, backgroundColor.getColor());
        text.draw();
    }
}
