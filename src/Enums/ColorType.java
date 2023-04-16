package Enums;

import java.awt.*;

public enum ColorType {
    DefaultBackground(new Color(122,1,23,128)),
    HoverBackground(new Color(14,100,23,128)),
    DefaultText(new Color(14,100,100,128)),
    HoverText(new Color(14,100,230,128));
    private final Color color;

    ColorType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
