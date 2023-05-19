package Scenes;

import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.ComponentType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a several DynamicComponents(specific to a scene).
 * It actualizes them, and it handles different kinds of request.
 * Note: each DynamicComponent has a reference to its scene.(see the Mediator Design Pattern)
 */
public abstract class Scene implements Notifiable {
    protected SceneHandler sceneHandler;
    protected List<StaticComponent> components;

    public Scene(SceneHandler sceneHandler) {
        this.sceneHandler = sceneHandler;
        components = new ArrayList<>();
    }

    public void update() throws Exception {
        for (int index = 0; index < components.size(); index++) {
            components.get(index).update();
        }
    }

    public void draw(Graphics2D graphics2D) {
        for (int index = 0; index < components.size(); index++) {
            StaticComponent component = components.get(index);
            components.get(index).draw(graphics2D);
        }
    }

    public SceneHandler getSceneHandler() {
        return sceneHandler;
    }

    /**
     * @param component to be added
     */
    public void addComponent(StaticComponent component) {
        components.add(component);
    }

    /**
     * @param component to be removed
     */
    public void removeComponent(StaticComponent component) {
        components.remove(component);
    }
}
