package Components.GameComponents.GameItems;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class encapsulates the behavior of a chest.
 */
public class Chest extends DynamicComponent {
    transient private AnimationHandler animationHandler;
    private final Map<ComponentStatus, Boolean> statuses;

    public Chest(Scene scene, Coordinate<Integer> position) {
        super();
        this.scene = scene;

        // chest statuses
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.IS_OPENED, false);
        statuses.put(ComponentStatus.HAS_DROPPED_WEAPON, false);

        // chest animation
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Chest1, new Coordinate<>(position));
        // lock at first frame -> chest in locked
        animationHandler.getAnimation().lockAtFistFrame();

        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) {
        if (message.source() == ComponentType.PLAYER) {
            if (message.type() == MessageType.READY_TO_BE_OPENED && !statuses.get(ComponentStatus.IS_OPENED)) {
                statuses.put(ComponentStatus.IS_OPENED, true);
                animationHandler.getAnimation().unlock();
            }
        }
    }

    @Override
    public void interactionWith(Object object) {}

    @Override
    public void update() {
        super.update();
        if (statuses.get(ComponentStatus.IS_OPENED) &&
                animationHandler.getAnimation().animationIsOver() &&
                !statuses.get(ComponentStatus.HAS_DROPPED_WEAPON)) {
            animationHandler.getAnimation().lockAtLastFrame();
            statuses.put(ComponentStatus.HAS_DROPPED_WEAPON, true);
            scene.notify(new Message(MessageType.SPAWN_GUN, ComponentType.CHEST, getId()));
        }
        animationHandler.update();
        scene.notify(new Message(MessageType.HANDLE_COLLISION, ComponentType.CHEST, getId()));
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
        return ComponentType.CHEST;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // chest animation
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Chest1, collideBox.getPosition());

        // restore the animation status
        if(statuses.get(ComponentStatus.IS_OPENED)){
            animationHandler.getAnimation().lockAtLastFrame();
        }else {
            animationHandler.getAnimation().lockAtFistFrame();
        }
        animationHandler.getAnimation().setRectangle(collideBox);
    }
}
