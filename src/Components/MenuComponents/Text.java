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
 *
 * @see StaticComponent
 * @see Serializable
 */
public class Text implements StaticComponent, Serializable {

    /**
     * This variable stores the text position
     */
    private final Coordinate<Integer> position;

    /**
     * This variable stores the color of the text
     */
    private ColorType textColor = ColorType.DEFAULT_TEXT;

    /**
     * This variable stores the content
     */
    private String text;

    /**
     * This variable stores the size.
     */
    private float size;

    /**
     * This variable stores the font.
     */
    private static Font font;

    static {
        try {
            String source = "Fonts/FutureMillennium.ttf";
            source = Objects.requireNonNull(Database.class.getClassLoader().getResource(source)).getPath();
            font = Font.createFont(TRUETYPE_FONT, new File(source));
        } catch (FontFormatException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This constructor creates a text component.
     */
    public Text(String text, Coordinate<Integer> position, float size) {
        this.text = text;
        this.position = position;
        this.size = size;
    }

    @Override
    public void update() {
    }

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

    /**
     * This method setts a new color to a specific text.
     *
     * @param color new color of the text
     */
    public void setTextColor(ColorType color) {
        textColor = color;
    }

    /**
     * This method sets a new size to the text.
     *
     * @param size new size of the text
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Getter for the text content.
     *
     * @return string text
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for the text content.
     *
     * @param text new content
     */
    public void setText(String text) {
        this.text = text;
    }

}