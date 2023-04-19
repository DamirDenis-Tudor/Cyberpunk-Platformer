package Components.MenuItems;

import Components.StaticComponent;
import Enums.ColorType;
import Utils.Coordinate;
import Window.GameWindow;

import java.io.Serializable;

public class Text implements StaticComponent, Serializable {
    private final Coordinate<Integer> position;
    private ColorType textColor = ColorType.DefaultText;
    private String text = "";
    private int size= 0;
    public Text(String text, Coordinate<Integer> position , int size){
        this.text = text;
        this.position = position;
        this.size = size;
    }
    @Override
    public void update() {}

    @Override
    public void draw() {
        GameWindow.get().drawText(text, position.getPosX(),
                position.getPosY(), textColor.getColor(), size);
    }

    public void setTextColor(ColorType color){
        textColor = color;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }
}