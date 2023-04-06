package Scenes;

import Components.DinamicComponents.DynamicComponent;
import Scenes.Messages.Message;
import Enums.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a several DynamicComponents(specific to a scene).
 * It actualizes them, and it handles different kinds of request.
 * Note : each DynamicComponent has a reference to its scene.(see the Mediator Design Pattern)
 */
public abstract class Scene {
    protected List<DynamicComponent> components;
    public Scene() {
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
     * This method can be called when a component make a request or when a scene decides.
     * @param newScene scene to be activated
     * @throws Exception
     */
    public void requestSceneChange(SceneType newScene) throws Exception {
        SceneHandler.getInstance().handleSceneChangeRequest(newScene);
    }

    /**
     * @param component to be added
     */
    public void addComponent(DynamicComponent component) {
        components.add(component);
    }

    public void removeComponent(DynamicComponent component){components.remove(component);}

    public boolean stillExists(DynamicComponent component){return components.contains(component);}
    /**
     * this method search for a specific component by an indentifier
     * @param name to be found
     * @return founded component
     */
    public DynamicComponent findComponent(ComponentType name){
        for (DynamicComponent dinamicComponent: components){
            if (name == dinamicComponent.getBaseType()){
                return dinamicComponent;
            }
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public DynamicComponent findComponentWithId(int id){
        for (DynamicComponent dinamicComponent: components){
            if (id == dinamicComponent.getId()){
                return dinamicComponent;
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
        for (DynamicComponent dinamicComponent: components){
            if (name == dinamicComponent.getBaseType()){
                searchedComponents.add(dinamicComponent);
            }
        }
        return searchedComponents;
    }
    /**
     * this method should handle all the components scene requests.
     * @param message to be handled
     * @throws Exception
     */
    public abstract void notify(Message message) throws Exception;
}
