package Components.DinamicComponents.Characters;

import Components.DinamicComponents.DinamicComponent;
import Scenes.Scene;

public abstract class Character extends DinamicComponent {

    public Character(Scene scene) throws Exception {
        this.scene = scene;
    }

    //
    public abstract void move() throws Exception;
}
