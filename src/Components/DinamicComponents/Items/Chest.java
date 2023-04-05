package Components.DinamicComponents.Items;

import Components.DinamicComponents.DynamicComponent;
import Components.StaticComponents.AnimationHandler;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;


public class Chest extends DynamicComponent {
    AnimationHandler animationHandler;
    private final Map<ComponentStatus, Boolean> statuses;

    public Chest(Scene scene, Coordinate<Integer> position) throws Exception {
        super();
        this.scene = scene;

        statuses = new HashMap<>();
        statuses.put(ComponentStatus.IsOpened, false);
        statuses.put(ComponentStatus.HasDropedWeapon, false);

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Chest1, position);
        animationHandler.getAnimation().lockAtFistFrame();
        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) throws Exception {
        if (message.getSource() == ComponentType.Player) {
            if (message.getType() == MessageType.ReadyToBeOpened && !statuses.get(ComponentStatus.IsOpened)) {
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
                !statuses.get(ComponentStatus.HasDropedWeapon)) {
            animationHandler.getAnimation().lockAtLastFrame();
            statuses.put(ComponentStatus.HasDropedWeapon ,true);
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
    public ComponentType getType() {
        return ComponentType.Chest;
    }
}
