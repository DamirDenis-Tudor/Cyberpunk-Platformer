package Components.MenuItems;

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
import Window.GameWindow;

import java.io.Serializable;

public class Button implements StaticComponent,Serializable {
    private final Scene scene;
    private final ComponentType currentType;
    private ColorType backgroundColor = ColorType.DefaultBackground;
    private final Rectangle collideBox;
    private final Text text;
    private boolean previousClicked = false;
    public Button(Scene scene, ComponentType type,String info, Rectangle collideBox , int size) {
        this.scene = scene;
        this.currentType = type;
        this.collideBox = collideBox;
        this.text = new Text(info, new Coordinate<>(collideBox.getCenterX() , collideBox.getCenterY()) , size);
    }
    @Override
    public void update(){
        if (collideBox.contains(MouseInput.get().getPosition())) {
            backgroundColor = ColorType.HoverBackground;
            text.setTextColor(ColorType.HoverText);
            if (MouseInput.get().isLeftMousePressed()) {
                if(!previousClicked) {
                    scene.notify(new Message(MessageType.ButtonClicked, currentType, -1));
                    previousClicked = true;
                    if (currentType == ComponentType.SaveInfo){
                        Database.get().setSaveToBeLoaded(text.getText().split(" ")[0]);
                    }
                }
            }else {
                previousClicked = false;
            }
        } else {
            backgroundColor = ColorType.DefaultBackground;
            text.setTextColor(ColorType.DefaultText);
        }
    }
    @Override
    public void draw() {
        GameWindow.get().drawRectangle(collideBox, backgroundColor.getColor());
        text.draw();
    }

    public Text getText(){
        return text;
    }
}
