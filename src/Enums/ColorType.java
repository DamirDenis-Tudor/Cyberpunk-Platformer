package Enums;

import java.awt.*;

public enum ColorType {
    DEFAULT_BACKGROUND(new Color(128, 128, 255, 128)),
    HOVER_BACKGROUND(new Color(204, 204, 255, 128)),
    DEFAULT_TEXT(new Color(224, 204, 255, 255)),
    HOVER_TEXT(new Color(224, 204, 255)),
    RED_COLOR(new Color(255, 0, 0)),
    BLACK_COLOR(new Color(89, 183, 255));
    private final Color color;

    ColorType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
