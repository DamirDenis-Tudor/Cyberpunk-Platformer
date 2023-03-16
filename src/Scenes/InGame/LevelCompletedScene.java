package Scenes.InGame;

import Components.DinamicComponents.Buttons.Button;
import Components.StaticComponents.StaticComponent;
import Scenes.Scene;
import States.State;

import java.util.ArrayList;
import java.util.List;

final public class LevelCompletedScene extends Scene {
    public LevelCompletedScene(State state){
        super(state);

        components.add(new Button(this));
    }
}
