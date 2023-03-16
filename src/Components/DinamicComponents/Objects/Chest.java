package Components.DinamicComponents.Objects;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import Scenes.Scene;
import Utils.Coordinate;

public class Chest extends DinamicComponent {
    private AssetsDeposit assetsDeposit;
    private Animation animation;

    public Chest(Scene scene, Coordinate<Integer> position) throws Exception {
        setScene(scene);
        assetsDeposit = AssetsDeposit.getInstance();

        animation = new Animation(assetsDeposit.getAnimation("Chest2"));
        animation.setPosition(position);
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
