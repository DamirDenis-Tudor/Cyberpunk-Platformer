package Scenes.InGame;

import Components.DinamicComponents.Characters.Character;
import Components.DinamicComponents.Characters.Player;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.StaticComponent;
import Scenes.Scene;

import java.util.ArrayList;
import java.util.List;


public class PlayScene extends Scene {
    List<StaticComponent> components;

    public PlayScene() throws Exception {
        components = new ArrayList<>();

        components.add(AssetsDeposit.getInstance().getGameMap("IndustrialCity"));
        components.add(new Player(this));
    }
    @Override
    public void draw() throws Exception {
        for (StaticComponent component : components) {
            component.draw();
        }
    }

    @Override
    public void update() throws Exception {
        for (StaticComponent component : components) {
            component.update();
        }
    }
}
