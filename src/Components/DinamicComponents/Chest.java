package Components.DinamicComponents;

import Components.StaticComponents.AnimationHandler;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

/**
 * This class encapsulate the behavior of a chest.
 */
public class Chest extends DynamicComponent {
    AnimationHandler animationHandler;
    private final Map<ComponentStatus, Boolean> statuses;

    public Chest(Scene scene, Coordinate<Integer> position) throws Exception {
        super();
        this.scene = scene;

        // chest statuses
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.IsOpened, false);
        statuses.put(ComponentStatus.HasDroppedWeapon, false);

        // chest animation
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Chest1, position);
        // lock at first frame -> chest in locked
        animationHandler.getAnimation().lockAtFistFrame();

        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) throws Exception {
        if (message.source() == ComponentType.Player) {
            if (message.type() == MessageType.ReadyToBeOpened && !statuses.get(ComponentStatus.IsOpened)) {
                statuses.put(ComponentStatus.IsOpened, true);
                animationHandler.getAnimation().unlock();
            }
        }
    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {}

    @Override
    public void update() throws Exception {
        if (statuses.get(ComponentStatus.IsOpened) &&
                animationHandler.getAnimation().animationIsOver() &&
                !statuses.get(ComponentStatus.HasDroppedWeapon)) {
            animationHandler.getAnimation().lockAtLastFrame();
            statuses.put(ComponentStatus.HasDroppedWeapon,true);
            scene.notify(new Message(MessageType.SpawnGun,ComponentType.Chest,getId()));
        }
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Chest,getId()));
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
        return ComponentType.Chest;
    }
}
