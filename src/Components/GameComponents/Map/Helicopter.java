package Components.GameComponents.Map;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
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

import static Utils.Constants.HELICOPTER_VELOCITY;

/**
 * This class describes the helicopter behavior.
 *
 * @see DynamicComponent
 */
public class Helicopter extends DynamicComponent {
    /**
     * Variable for animation wrapper specific to a helicopter.
     */
    transient protected AnimationHandler animationHandler;

    /**
     * Collection that stores supported statuses.
     */
    protected final Map<ComponentStatus, Boolean> statuses;

    /**
     * Variable that stores the initial position.
     * It helps to determine when max distance has elapsed.
     */
    private final Coordinate<Integer> initialPosition;

    /**
     * This constructor initializes all the important fields.
     *
     * @param scene    reference to the component that must be notified.
     * @param position component start position.
     */

    public Helicopter(Scene scene, Coordinate<Integer> position) {
        super();
        this.scene = scene;
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.TOP_COLLISION, false);
        statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
        statuses.put(ComponentStatus.HAS_PLAYER, false);

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Helicopter, new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();
        initialPosition = new Coordinate<>(collideBox.getPosition());
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case MAP -> {
                switch (message.type()) {
                    case ACTIVATE_TOP_COLLISION -> {
                        statuses.put(ComponentStatus.TOP_COLLISION, true);
                        statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
                        animationHandler.getAnimation().setDirection(true);
                    }
                }
            }
            case PLAYER -> {
                if (message.type() == MessageType.ON_HELICOPTER) {
                    statuses.put(ComponentStatus.HAS_PLAYER, true);
                } else if (message.type() == MessageType.DETACHED_FROM_HELICOPTER) {
                    statuses.put(ComponentStatus.HAS_PLAYER, false);
                }

            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        if (component.getGeneralType() == ComponentType.PLAYER) {
            if (statuses.get(ComponentStatus.HAS_PLAYER)) {
                component.getCollideBox().getPosition().setX(collideBox.getCenterX() - component.getCollideBox().getWidth() / 2);
                component.getCollideBox().getPosition().setY(collideBox.getMaxY() - 28);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!statuses.get(ComponentStatus.TOP_COLLISION)) {
            collideBox.moveByY(-HELICOPTER_VELOCITY);
        } else if (!statuses.get(ComponentStatus.BOTTOM_COLLISION)) {
            collideBox.moveByY(HELICOPTER_VELOCITY);
        }
        if (initialPosition.getPosY() < collideBox.getPosition().getPosY()) {
            statuses.put(ComponentStatus.TOP_COLLISION, false);
            statuses.put(ComponentStatus.BOTTOM_COLLISION, true);
            animationHandler.getAnimation().setDirection(true);
        }
        scene.notify(new Message(MessageType.HANDLE_COLLISION, ComponentType.HELICOPTER, getId()));
        animationHandler.update();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (!getActiveStatus()) return;
        animationHandler.draw(graphics2D);
    }

    @Override
    public void addMissingPartsAfterDeserialization(Notifiable scene) {
        super.addMissingPartsAfterDeserialization(scene);
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Helicopter, collideBox.getPosition());
    }

    @Override
    public ComponentType getCurrentType() {
        return ComponentType.NONE;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.HELICOPTER;
    }
}

