package Enums;

import java.awt.*;

public enum ColorType {
    DefaultBackground(new Color(128, 128, 255,128)),
    HoverBackground(new Color(204, 204, 255,128)),
    DefaultText(new Color(224, 204, 255,255)),
    HoverText(new Color(224, 204, 255)),
    RedColor(new Color(255, 0, 0,128));
    private final Color color;

    ColorType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
