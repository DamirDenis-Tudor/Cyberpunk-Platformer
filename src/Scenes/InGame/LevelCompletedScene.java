package Scenes.InGame;

import Components.Button;
import Components.Component;
import Scenes.Scene;

import java.util.ArrayList;
import java.util.List;

public class LevelCompletedScene extends Scene {
    List<Component> components;

    public LevelCompletedScene(){
        components = new ArrayList<>();

        components.add(new Button(this));
    }
    @Override
    public void draw() {
      //  System.out.println("LevelCompletedScene draw.");
    }

    @Override
    public void update() throws Exception {
        // System.out.println("LevelCompletedScene update.");
        for (Component component:components) {
            component.update();
        }
    }
}
