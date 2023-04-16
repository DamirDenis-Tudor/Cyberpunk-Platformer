package Scenes;

import Components.DynamicComponents.DynamicComponent;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.*;

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
        for (int index = 0; index < components.size() ; index++) {
            components.get(index).update();
        }
    }
    public void draw() {
        for (int index = 0; index < components.size() ; index++) {
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
    public void removeComponent(StaticComponent component){components.remove(component);}

    /**
     * @param component to be checked
     * @return return the existence status
     */
    public boolean stillExists(DynamicComponent component){return components.contains(component);}
    /**
     * this method search for a specific component by an identifier
     * @param name to be found
     * @return founded component
     */
    public DynamicComponent findComponent(ComponentType name){
        for (StaticComponent component: components){
            DynamicComponent dynamicComponent = (DynamicComponent) component;
            if (name == dynamicComponent.getGeneralType()){
                return dynamicComponent;
            }
        }
        return null;
    }

    /**
     * @param id specific identifier of the component
     * @return null or founded component
     */
    public DynamicComponent findComponentWithId(int id){
        for (StaticComponent component: components){
            DynamicComponent dynamicComponent = (DynamicComponent) component;
            if (id == dynamicComponent.getId()){
                return dynamicComponent;
            }
        }
        return null;
    }

    /**
     * this method selects all the components with the given name
     * @param name given name
     * @return list of components
     */
    public List<DynamicComponent> getAllComponentsWithName(ComponentType name){
        List<DynamicComponent> searchedComponents= new ArrayList<>();
        for (StaticComponent component: components){
            DynamicComponent dynamicComponent = (DynamicComponent) component;
            if (name == dynamicComponent.getGeneralType()){
                searchedComponents.add(dynamicComponent);
            }
        }
        return searchedComponents;
    }
}
