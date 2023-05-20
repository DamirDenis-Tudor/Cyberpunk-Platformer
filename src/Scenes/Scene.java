package Scenes;

import Components.Notifiable;
import Components.StaticComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a several StaticComponents(specific to a scene).
 * It actualizes them, and it handles different kinds of request.
 * @see Notifiable
 * @see StaticComponent
 */
public abstract class Scene implements Notifiable {
    /**Reference to its handler.*/
    protected SceneHandler sceneHandler;

    /**List of abstract components.*/
    protected List<StaticComponent> components;

    /**
     * Constructor for an abstract scene.
     * @param sceneHandler reference to its handler
     */
    public Scene(SceneHandler sceneHandler) {
        this.sceneHandler = sceneHandler;
        components = new ArrayList<>();
    }

    /**
     * This method updates all components.
     * @throws Exception
     */
    public void update() throws Exception {
        for (int index = 0; index < components.size(); index++) {
            components.get(index).update();
        }
    }

    /**
     * This method draws all components.
     * @param graphics2D : graphic context
     */
    public void draw(Graphics2D graphics2D) {
        for (int index = 0; index < components.size(); index++) {
            StaticComponent component = components.get(index);
            components.get(index).draw(graphics2D);
        }
    }

    /**
     * Getter for handler reference
     * @return scene handler
     */
    public SceneHandler getSceneHandler() {
        return sceneHandler;
    }

    /**
     * This method adds a new component.
     * @param component to be added
     */
    public void addComponent(StaticComponent component) {
        components.add(component);
    }

    /**
     * This method removes a component.
     * @param component to be removed
     */
    public void removeComponent(StaticComponent component) {
        components.remove(component);
    }
}
