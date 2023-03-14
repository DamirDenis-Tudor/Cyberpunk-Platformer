package Components.DinamicComponents.Characters;

import GameWindow.Camera;
import Input.KeyboardInput;
import Scenes.Scene;

public class Player extends Character{
    private Camera camera ;
    private KeyboardInput keyboardInput;

    public Player(Scene scene) throws Exception {
        super(scene);
        camera = Camera.getInstance();
        keyboardInput = KeyboardInput.getInstance();
    }

    @Override
    public void move() {
        if (KeyboardInput.getInstance().getKeyA()){
            camera.setCurrentXoffset(5);
        }
        if (KeyboardInput.getInstance().getKeyD()){
            camera.setCurrentXoffset(-5);
        }
    }

    @Override
    public void update() throws Exception {
        //requestSceneChange("LevelPauseScene");
        move();
    }

    @Override
    public void draw() {

    }
}
