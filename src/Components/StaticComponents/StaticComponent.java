package Components.StaticComponents;

/**
 * This interface describes the basic functionality of a static object, such as a map, a tile, or animations.
 * These components should not make changes to the course of the game.
 */
public interface StaticComponent {

    void update() throws Exception;

    void draw();
}
