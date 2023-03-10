package Scenes.InGame;

import Components.Character;
import Components.Component;
import Scenes.Scene;

import java.util.ArrayList;
import java.util.List;


public class PlayScene extends Scene {
    List<Component> components;

    public PlayScene() throws Exception {
        components = new ArrayList<>();

        components.add(new Character(this));

    }
    @Override
    public void draw() throws Exception {

    }

    @Override
    public void update() throws Exception {
        for (Component component:components) {
            component.update();
        }
    }
}
