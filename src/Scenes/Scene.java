package Scenes;

import Components.DinamicComponents.DinamicComponent;
import Scenes.Messages.Message;
import Enums.*;
import Utils.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a several DinamicComponents(specific to a scene).
 * It actualizes them, and it handles different kinds of request.
 * Note : each DinamicComponent has a reference to its scene.(see the Mediator Design Pattern)
 */
public abstract class Scene {
    protected List<DinamicComponent> components;
    public Scene() {
        components = new ArrayList<>();
    }

    public void update() throws Exception {
        for (DinamicComponent component : components) {
            component.update();
        }
    }
    public void draw() {
        for (DinamicComponent component : components) {
            component.draw();
        }
    }

    /**
     * This method can be called when a component make a request or when a scene decides.
     * @param newScene scene to be activated
     * @throws Exception
     */
    public void requestSceneChange(SceneNames newScene) throws Exception {
        SceneHandler.getInstance().handleSceneChangeRequest(newScene);
    }

    /**
     * @param component to be added
     */
    public void addComponent(DinamicComponent component) {
        components.add(component);
    }

    /**
     * this method search for a specific component by an indentifier
     * @param name to be found
     * @return founded component
     */
    public DinamicComponent findComponent(ComponentNames name){
        for (DinamicComponent dinamicComponent: components){
            if (name == dinamicComponent.getType()){
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
    public List<DinamicComponent> getAllComponentsWithName(ComponentNames name){
        List<DinamicComponent> searchedComponents= new ArrayList<>();
        for (DinamicComponent dinamicComponent: components){
            if (name == dinamicComponent.getType()){
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
