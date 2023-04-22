package Scenes;

import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.ComponentType;

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

    public void draw() {
        for (int index = 0; index < components.size(); index++) {
            components.get(index).draw();
        }
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

    /**
     * @param component to be checked
     * @return return the existence status
     */
    public boolean stillExists(DynamicComponent component) {
        return components.contains(component);
    }

    /**
     * this method search for a specific component
     *
     * @param name to be found
     * @return founded component
     */
    public DynamicComponent findComponentWithName(ComponentType name) {
        try {
            for (StaticComponent component : components) {
                if (!(component instanceof DynamicComponent dynamicComponent)) {
                    throw new ClassCastException("Component " + component.getClass() + " cannot be casted to DynamicComponent.");
                }
                if (name == dynamicComponent.getGeneralType()) {
                    return dynamicComponent;
                }
            }
            throw new ClassNotFoundException("Dynamic component not found");
        } catch (ClassCastException | ClassNotFoundException e) {
            throw new RuntimeException("Error searching for dynamic component " + name.name() + " : " + e.getMessage(), e);
        }
    }


    /**
     * @param id specific identifier of the component
     * @return null or founded component
     */
    public DynamicComponent findComponentWithId(int id) {
        try {
            for (StaticComponent component : components) {
                if (!(component instanceof DynamicComponent dynamicComponent)) {
                    throw new ClassCastException("Component " + component.getClass()+ " cannot be casted to DynamicComponent.");
                }
                if (id == dynamicComponent.getId()) {
                    return dynamicComponent;
                }
            }
            throw new ClassNotFoundException("Dynamic component not found");
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("Error searching for dynamic component " + id + " : " + e.getMessage());
        }
        return null;
    }

    /**
     * this method selects all the components with the given name
     *
     * @param name given name
     * @return list of components
     */
    public List<DynamicComponent> getAllComponentsWithName(ComponentType name) {
        try {
            List<DynamicComponent> searchedComponents = new ArrayList<>();
            for (StaticComponent component : components) {
                if (!(component instanceof DynamicComponent dynamicComponent)) {
                    throw new ClassCastException("Component "+component.getClass()+ " cannot be casted to DynamicComponent.");
                }
                if (name == dynamicComponent.getGeneralType()) {
                    searchedComponents.add(dynamicComponent);
                }
            }
            return searchedComponents;
        } catch (ClassCastException e) {
            throw new RuntimeException("Error searching for dynamic component: " + e.getMessage(), e);
        }
    }
}
