package Scenes;

import Components.DinamicComponents.DinamicComponent;
import Scenes.Messages.Message;
import Enums.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    List<DinamicComponent> components;
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

    public void requestSceneChange(SceneNames newScene) throws Exception {
        SceneHandler.getInstance().handleSceneChangeRequest(newScene);
    }

    public void addComponent(DinamicComponent component) {
        components.add(component);
    }

    public abstract void notify(Message message) throws Exception;

    public abstract void saveState() ;
    public abstract void loadState() ;

    public abstract void resetState() ;
}
