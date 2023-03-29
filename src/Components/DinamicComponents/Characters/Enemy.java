package Components.DinamicComponents.Characters;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import Enums.AnimationNames;
import Enums.ComponentNames;
import Enums.MessageNames;
import Input.KeyboardInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;

public class Enemy extends DinamicComponent {
    private Animation animation;
    private boolean bottomCollision = false;

    private boolean doubleJump = false;

    public Enemy(Scene scene, Coordinate<Integer> position) throws Exception {
        this.scene = scene;

        animation = new Animation(AssetsDeposit.getInstance().getAnimation(AnimationNames.Enemy1Idle));
        animation.setPosition(position);

        // takes a "reference" of the animation rectangle
        collideBox = animation.getRectangle();
    }

    @Override
    public void notify(Message message) throws Exception {

    }

    @Override
    public void update() throws Exception {
        collideBox.moveByY(8);
        animation.update();
        scene.notify(new Message(MessageNames.HandleCollision , ComponentNames.Enemy));
    }

    @Override
    public void draw() {
        animation.draw();
    }

    @Override
    public ComponentNames getType() {
        return ComponentNames.Enemy;
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) {

    }
}
