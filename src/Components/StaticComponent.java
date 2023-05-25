package Components;

import Enums.ComponentType;

import java.awt.*;

/**
 * This interface describes the basic functionality of a static object.
 */
public interface StaticComponent {
    void update();

    void draw(Graphics2D graphics2D);
}
