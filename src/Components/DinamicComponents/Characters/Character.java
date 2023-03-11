package Components.DinamicComponents.Characters;

import Components.DinamicComponents.DinamicComponent;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;

public abstract class Character extends DinamicComponent {

    public Character(Scene scene) throws Exception {
        setScene(scene);
    }

    public abstract void move();
}
