package Scenes;

import Components.StaticComponents.StaticComponent;
import States.State;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final State state;

    protected List<StaticComponent> components;

    public Scene(State state){
        this.state = state;
        components = new ArrayList<>();
    }

    public void draw() throws Exception {
        for (StaticComponent component : components) {
            component.draw();
        }
    }

    public void update() throws Exception {
        for (StaticComponent component : components) {
            component.update();
        }
    }

    public void requestSceneChange(String newScene) throws Exception {
        state.requestSceneChange(newScene);
    }


    public void saveState(){

    }
    public void loadState(){

    }
    public void resetState(){

    }

}
