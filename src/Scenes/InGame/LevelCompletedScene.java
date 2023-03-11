package Scenes.InGame;

import Components.DinamicComponents.Buttons.Button;
import Components.StaticComponents.StaticComponent;
import Scenes.Scene;

import java.util.ArrayList;
import java.util.List;

public class LevelCompletedScene extends Scene {
    List<StaticComponent> components;

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
        for (StaticComponent component : components) {
            component.update();
        }
    }
}
