package Components.GameComponents.Map;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static Utils.Constants.PLATFORM_VELOCITY;

/**
 * This class describes the platform behavior.The code might be complicated, but it is not.
 * It is nothing more than a state machine that describes the interactions with other components.
 */
public class Platform extends DynamicComponent {
    transient protected AnimationHandler animationHandler;
    protected final Map<ComponentStatus, Boolean> statuses;

    private AnimationType animationType;

    public Platform(Scene scene, Coordinate<Integer> position , AnimationType animationType) {
        super();
        this.scene = scene;

        this.animationType = animationType;
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.LEFT_COLLISION, false);
        statuses.put(ComponentStatus.RIGHT_COLLISION, false);

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(animationType, new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case MAP, PLATFORM -> {
                switch (message.type()) {
                    case LEFT_COLLISION -> {
                        statuses.put(ComponentStatus.LEFT_COLLISION, true);
                        statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                        animationHandler.getAnimation().setDirection(true);
                    }
                    case RIGHT_COLLISION -> {
                        statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                        statuses.put(ComponentStatus.LEFT_COLLISION, false);
                        animationHandler.getAnimation().setDirection(false);
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        if (component.getGeneralType() == ComponentType.PLATFORM) {
            collideBox.solveCollision(component.getCollideBox());
            if (collideBox.getDx() > 0) {
                component.notify(new Message(MessageType.RIGHT_COLLISION, ComponentType.GROUND_ENEMY, getId()));
                statuses.put(ComponentStatus.LEFT_COLLISION, true);
                statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                animationHandler.getAnimation().setDirection(true);
            } else if (collideBox.getDx() < 0) {
                component.notify(new Message(MessageType.LEFT_COLLISION, ComponentType.GROUND_ENEMY, getId()));
                statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                statuses.put(ComponentStatus.LEFT_COLLISION, false);
                animationHandler.getAnimation().setDirection(false);
            }
        } else if (component.getGeneralType() == ComponentType.PLAYER) {
            if(animationHandler.getAnimation().getDirection()){
                component.getCollideBox().moveByX(PLATFORM_VELOCITY);
            } else  {
                component.getCollideBox().moveByX(-PLATFORM_VELOCITY);
            }

        }
    }

    @Override
    public void update() {
        super.update();
        if (!statuses.get(ComponentStatus.LEFT_COLLISION)) {
            collideBox.moveByX(-PLATFORM_VELOCITY);
        } else if (!statuses.get(ComponentStatus.RIGHT_COLLISION)) {
            collideBox.moveByX(PLATFORM_VELOCITY);
        }
        scene.notify(new Message(MessageType.HANDLE_COLLISION, ComponentType.PLATFORM, getId()));
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
        return ComponentType.PLATFORM;
    }

    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // restore animation handler
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(animationType, collideBox.getPosition());
        collideBox = animationHandler.getAnimation().getRectangle();

        // restore animation direction
        if (statuses.get(ComponentStatus.LEFT_COLLISION)){
            animationHandler.getAnimation().setDirection(true);
        }else if (statuses.get(ComponentStatus.RIGHT_COLLISION)){
            animationHandler.getAnimation().setDirection(false);
        }
    }
}
