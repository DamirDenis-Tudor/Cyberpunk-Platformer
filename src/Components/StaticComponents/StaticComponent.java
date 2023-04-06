package Components.StaticComponents;

/**
 * This interface describes the basic functionality of a static object.
 * These components should not make changes to the course of the game.
 */
interface StaticComponent {
    void update() throws Exception;
    void draw();
}
