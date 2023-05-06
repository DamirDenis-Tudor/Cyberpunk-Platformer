package Components.GameComponents.Map;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Components.GameComponents.GameItems.Gun;
import Enums.AnimationType;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.helicopterVelocity;

/**
 * This class describes the helicopter behaviour.
 * The code might be complicated, but it is not.
 * It is nothing more than a state machine that describes the interactions with other components.
 */
public class Helicopter extends DynamicComponent {
    transient protected AnimationHandler animationHandler;
    protected final Map<ComponentStatus, Boolean> statuses;
    private final Coordinate<Integer> initialPosition;

    public Helicopter(Scene scene, Coordinate<Integer> position) {
        super();
        this.scene = scene;
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.TopCollision, false);
        statuses.put(ComponentStatus.BottomCollision, false);
        statuses.put(ComponentStatus.HasPlayer, false);

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Helicopter, new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();
        initialPosition = new Coordinate<>(collideBox.getPosition());
    }

    @Override
    public void notify(Message message)  {
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
                } else if (message.type() == MessageType.DetachedFromHelicopter) {
                    statuses.put(ComponentStatus.HasPlayer, false);
                }

            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        if (component.getGeneralType() == ComponentType.Player) {
            if (statuses.get(ComponentStatus.HasPlayer)) {
                component.getCollideBox().getPosition().setX(collideBox.getCenterX() - component.getCollideBox().getWidth() / 2);
                component.getCollideBox().getPosition().setY(collideBox.getMaxY()-28);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!statuses.get(ComponentStatus.TopCollision)) {
            collideBox.moveByY(-helicopterVelocity);
        } else if (!statuses.get(ComponentStatus.BottomCollision)) {
            collideBox.moveByY(helicopterVelocity);
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
    public void draw(Graphics2D graphics2D) {
        if(!getActiveStatus()) return;
        animationHandler.draw(graphics2D);
    }

    @Override
    public ComponentType getCurrentType() {
        return null;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.Helicopter;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Helicopter, collideBox.getPosition());
    }
}

