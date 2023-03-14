package Components.DinamicComponents.Characters;

import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import GameWindow.Camera;
import Input.KeyboardInput;
import Scenes.Scene;

public class Player extends Character {
    private Camera camera;
    private KeyboardInput keyboardInput;

    private AssetsDeposit assetsDeposit;
    private Animation animation;

    public Player(Scene scene) throws Exception {
        super(scene);
        camera = Camera.getInstance();
        keyboardInput = KeyboardInput.getInstance();
        assetsDeposit = AssetsDeposit.getInstance();

        animation = assetsDeposit.getAnimation("BikerIdle");
        //animation.drawInMirror(true);
    }

    @Override
    public void move() {
        if (KeyboardInput.getInstance().getKeyA()) {
            camera.setCurrentXoffset(5);

            animation = assetsDeposit.getAnimation("BikerRun");
            animation.drawInMirror(true);


        } else if (KeyboardInput.getInstance().getKeyD()) {
            camera.setCurrentXoffset(-5);
            animation = assetsDeposit.getAnimation("BikerRun");
            animation.drawInMirror(false);

        }
    }

        @Override
        public void update () throws Exception {
            //requestSceneChange("LevelPauseScene");
            move();
            animation.update();
        }

        @Override
        public void draw () {
            animation.draw();
        }
    }
