package Components.DinamicComponents.Characters;

import Components.DinamicComponents.Objects.Chest;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import Scenes.Scene;
import Utils.Coordinate;

public class Animal extends Character {
    private AssetsDeposit assetsDeposit;
    private Animation animation;

    public Animal(Scene scene, Coordinate<Integer> position) throws Exception {
        super(scene);
        assetsDeposit = AssetsDeposit.getInstance();
        animation = new Animation(assetsDeposit.getAnimation("Dog1Idle"));
        animation.setPosition(position);
    }

    @Override
    public void move() {

    }

    @Override
    public void update() throws Exception {
        animation.update();
    }

    @Override
    public void draw() {
        animation.draw();
    }
}
