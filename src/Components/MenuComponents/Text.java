package Components.MenuComponents;

import Components.StaticComponent;
import Database.Database;
import Enums.ColorType;
import Utils.Coordinate;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import static java.awt.Font.TRUETYPE_FONT;

/**
 * This class encapsulates the text drawing behavior.
 */
public class Text implements StaticComponent, Serializable {
    private final Coordinate<Integer> position;
    private ColorType textColor = ColorType.DEFAULT_TEXT;
    private String text;
    private float size;
    private static Font font;

    static {
        try {
            String source = "Fonts/FutureMillennium.ttf";
            source = Objects.requireNonNull(Database.class.getClassLoader().getResource(source)).getPath();
            font = Font.createFont( TRUETYPE_FONT , new File(source) );
        } catch (FontFormatException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Text(String text, Coordinate<Integer> position , float size){
        this.text = text;
        this.position = position;
        this.size = size;
    }
    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D) {

        font = font.deriveFont(this.size);
        graphics2D.setFont(font);
        graphics2D.setColor(textColor.getColor());

        FontMetrics fm = graphics2D.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = position.getPosX() - textWidth / 2;
        int y = position.getPosY() - textHeight / 2 + fm.getAscent();

        graphics2D.drawString(text, x, y);
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

    public void setText(String text) {
        this.text = text;
    }
}