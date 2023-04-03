package Components.DinamicComponents.Characters.Animals;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import Enums.AnimationType;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

public class Animal extends DinamicComponent {
    private Animation animation;
    private boolean bottomCollision = false;

    private boolean doubleJump = false;

    public Animal(Scene scene, Coordinate<Integer> position) throws Exception {
        this.scene = scene;

        animation = new Animation(AssetsDeposit.getInstance().getAnimation(AnimationType.Dog1Idle));
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
        scene.notify(new Message(MessageType.HandleCollision , ComponentType.GroundAnimal));
    }

    @Override
    public void draw() {
        animation.draw();
    }

    @Override
    public ComponentType getType() {
        return ComponentType.GroundAnimal;
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) {

    }
}
