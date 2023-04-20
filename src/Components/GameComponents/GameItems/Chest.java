package Components.GameComponents.GameItems;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

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
        statuses.put(ComponentStatus.IsOpened, false);
        statuses.put(ComponentStatus.HasDroppedWeapon, false);

        // chest animation
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Chest1, new Coordinate<>(position));
        // lock at first frame -> chest in locked
        animationHandler.getAnimation().lockAtFistFrame();

        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) {
        if (message.source() == ComponentType.Player) {
            if (message.type() == MessageType.ReadyToBeOpened && !statuses.get(ComponentStatus.IsOpened)) {
                statuses.put(ComponentStatus.IsOpened, true);
                animationHandler.getAnimation().unlock();
            }
        }
    }

    @Override
    public void interactionWith(Object object) {}

    @Override
    public void update() {
        if (statuses.get(ComponentStatus.IsOpened) &&
                animationHandler.getAnimation().animationIsOver() &&
                !statuses.get(ComponentStatus.HasDroppedWeapon)) {
            animationHandler.getAnimation().lockAtLastFrame();
            statuses.put(ComponentStatus.HasDroppedWeapon, true);
            scene.notify(new Message(MessageType.SpawnGun, ComponentType.Chest, getId()));
        }
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Chest, getId()));
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentType getCurrentType() {
        return null;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.Chest;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // chest animation
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Chest1, collideBox.getPosition());

        // restore the animation status
        if(statuses.get(ComponentStatus.IsOpened)){
            animationHandler.getAnimation().lockAtLastFrame();
        }else {
            animationHandler.getAnimation().lockAtFistFrame();
        }
        animationHandler.getAnimation().setRectangle(collideBox);
    }
}
