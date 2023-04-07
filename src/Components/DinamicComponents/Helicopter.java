package Components.DinamicComponents;

import Components.StaticComponents.AnimationHandler;
import Enums.AnimationType;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.platformVelocity;

public class Helicopter extends DynamicComponent {
    protected final AnimationHandler animationHandler;
    protected final Map<ComponentStatus, Boolean> statuses;

    private final Coordinate<Integer> initialPosition;

    public Helicopter(Scene scene, Coordinate<Integer> position) throws Exception {
        super();
        this.scene = scene;
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.TopCollision, false);
        statuses.put(ComponentStatus.BottomCollision, false);
        statuses.put(ComponentStatus.HasPlayer, false);

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Helicopter, position);
        collideBox = animationHandler.getAnimation().getRectangle();
        initialPosition = new Coordinate<>(collideBox.getPosition());
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.source()) {
            case Map -> {
                switch (message.type()) {
                    case ActivateTopCollision -> {
                        statuses.put(ComponentStatus.TopCollision, true);
                        statuses.put(ComponentStatus.BottomCollision, false);
                        animationHandler.getAnimation().setDirection(true);
                    }
                }
            }
            case Player -> {
                if (message.type() == MessageType.OnHelicopter) {
                    statuses.put(ComponentStatus.HasPlayer, true);
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {
        if (component.getBaseType() == ComponentType.Player) {
            if (statuses.get(ComponentStatus.HasPlayer)) {
                component.getCollideBox().getPosition().setX(collideBox.getCenterX() - component.getCollideBox().getWidth() / 2);
                component.getCollideBox().getPosition().setY(collideBox.getMaxY() - 25);
            }
        }
    }

    @Override
    public void update() throws Exception {
        if (!statuses.get(ComponentStatus.TopCollision)) {
            collideBox.moveByY(-platformVelocity);
        } else if (!statuses.get(ComponentStatus.BottomCollision)) {
            collideBox.moveByY(platformVelocity);
        }
        if(initialPosition.getPosY() < collideBox.getPosition().getPosY()) {
            statuses.put(ComponentStatus.TopCollision, false);
            statuses.put(ComponentStatus.BottomCollision, true);
            animationHandler.getAnimation().setDirection(true);
        }
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Helicopter, getId()));
        animationHandler.update();
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentType getSubType() {
        return null;
    }

    @Override
    public ComponentType getBaseType() {
        return ComponentType.Helicopter;
    }
}

