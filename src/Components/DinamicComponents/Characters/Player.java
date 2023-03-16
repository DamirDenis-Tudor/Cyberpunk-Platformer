package Components.DinamicComponents.Characters;

import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import GameWindow.Camera;
import Input.KeyboardInput;
import Scenes.Scene;
import Utils.Coordinate;

public class Player extends Character {
    private Camera camera;
    private KeyboardInput keyboardInput;
    private AssetsDeposit assetsDeposit;
    private Animation animation;

    public Player(Scene scene, Coordinate<Integer> position) throws Exception {
        super(scene);
        camera = Camera.getInstance();
        keyboardInput = KeyboardInput.getInstance();
        assetsDeposit = AssetsDeposit.getInstance();

        animation = new Animation(assetsDeposit.getAnimation("PunkAttack3"));
        animation.setPosition(position);
    }

    @Override
    public void move() throws Exception {
        if (KeyboardInput.getInstance().getKeyA()) {
            camera.setCurrentXoffset(5);
        } else if (KeyboardInput.getInstance().getKeyD()) {
            camera.setCurrentXoffset(-5);
        }
    }
    @Override
    public void update() throws Exception {
        move();
        animation.update();
    }

    @Override
    public void draw() {
        animation.draw();
    }
}
