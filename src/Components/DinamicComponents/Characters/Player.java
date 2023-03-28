package Components.DinamicComponents.Characters;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import Enums.ComponentNames;
import Enums.MessageNames;
import Scenes.Messages.Message;
import Scenes.Scene;
import Window.Camera;
import Input.KeyboardInput;
import Enums.AnimationNames;
import Utils.Coordinate;
public class Player extends DinamicComponent {
    private final KeyboardInput keyboardInput;
    private final Animation animation;

    public Player(Scene scene, Coordinate<Integer> position) throws Exception {
        this.scene = scene;

        Camera camera = Camera.getInstance();
        keyboardInput = KeyboardInput.getInstance();
        AssetsDeposit assetsDeposit = AssetsDeposit.getInstance();

        animation = new Animation(assetsDeposit.getAnimation(AnimationNames.BikerDash));
        animation.setPosition(position);

        // takes a "reference" of the animation rectangle
        collideBox = animation.getRectancle();
    }

    @Override
    public void notify(Message message) {
        /*
            different kinds of messages that means something:
            -> has been hit;
            -> has picked up something;
            and so on
         */
    }

    @Override
    public void update() throws Exception {
        // movement
        if (keyboardInput.getKeyD()){
            collideBox.moveByX(5);
        }
        if (keyboardInput.getKeyA()){
            collideBox.moveByX(-5);
        }
        if (keyboardInput.getKeyW()){
            collideBox.moveByY(-5);
        }
        if (keyboardInput.getKeyS()){
            collideBox.moveByY(5);
        }

        // make a request to handle the collisions
        scene.notify(new Message(MessageNames.HandleCollision , ComponentNames.Player));

        animation.update();
    }

    @Override
    public void draw() {
        animation.draw();
    }

    @Override
    public ComponentNames getType() {
        return ComponentNames.Player;
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) {
        // here will have the opportunity to handle interactions;
    }
}
