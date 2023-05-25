package Components.GameComponents.GameItems;

import Components.BaseComponents.AssetDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.GameComponents.CharacterisesGenerator;
import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimerHandler;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class describes the gun behaviors.
 *
 * @see DynamicComponent
 */
public class Gun extends DynamicComponent {
    /**
     * Variable for buffered image wrapper specific to a gun bullet.
     */
    transient private ImageWrapper imageWrapper;

    /**
     * Collection that stores supported statuses.
     */
    private final Map<ComponentStatus, Boolean> statuses;

    /**
     * Variable that stores the initial position.
     * When the gun will be dropped, it will be placed automatically at the spawn position.
     */
    private Coordinate<Integer> initialPosition;

    /**
     * Variable that stores the current gun type.
     */
    private ComponentType gunType = null;

    /**
     * Variable that stores the bullet direction.
     */
    private boolean direction = true;

    /**
     * Variables for the drawing on screen offset.
     */
    private int xOffset = 10, yOffset = 0;

    /**
     * This is a constructor a specific gun instance.
     *
     * @param image      its related image
     * @param collideBox its related intersection box
     */
    public Gun(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
        statuses = new HashMap<>();
    }

    /**
     * This is a copy constructor of an existing gun.
     *
     * @param scene    component that needs to be notified.
     * @param gunType  gun related type.
     * @param position gun starting position.
     */
    public Gun(Scene scene, Coordinate<Integer> position, ComponentType gunType) {
        this.scene = scene;
        this.gunType = gunType;
        this.imageWrapper = AssetDeposit.get().getGun(gunType).imageWrapper;
        this.collideBox = new Rectangle(AssetDeposit.get().getGun(gunType).collideBox);
        this.collideBox.setPosition(new Coordinate<>(position));
        statuses = CharacterisesGenerator.generateStatusesFor(ComponentType.GUN);
        TimerHandler.get().addTimer(new Timer(0.3f), "GUN" + getId());

    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case PLAYER -> {
                switch (message.type()) {
                    case LAUNCH_BULLET -> {
                        if (statuses.get(ComponentStatus.GUN_ENABLED))
                            statuses.put(ComponentStatus.HAS_LAUNCHED_BULLET, true);
                    }
                    case PLAYER_DIRECTION_LEFT -> {
                        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
                            direction = false;
                            xOffset = 35;
                        }
                    }
                    case PLAYER_DIRECTION_RIGHT -> {
                        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
                            direction = true;
                            xOffset = 27;
                        }
                    }
                    case HIDE_GUN -> {
                        if (statuses.get(ComponentStatus.IS_PICKED_UP) && statuses.get(ComponentStatus.GUN_ENABLED))
                            statuses.put(ComponentStatus.HIDE, true);
                    }
                    case SHOW_GUN -> {
                        if (statuses.get(ComponentStatus.IS_PICKED_UP) && statuses.get(ComponentStatus.GUN_ENABLED))
                            statuses.put(ComponentStatus.HIDE, false);
                    }
                    case SHOOT -> {
                        if (statuses.get(ComponentStatus.IS_PICKED_UP) && statuses.get(ComponentStatus.GUN_ENABLED)) {
                            if (direction) {
                                scene.notify(new Message(MessageType.BULLET_LAUNCH_RIGHT, ComponentType.GUN, getId()));
                            } else {
                                scene.notify(new Message(MessageType.BULLET_LAUNCH_LEFT, ComponentType.GUN, getId()));
                            }
                        }
                    }
                }
            }
            case SCENE -> {
                switch (message.type()) {
                    case DISABLE_GUN -> {
                        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
                            statuses.put(ComponentStatus.GUN_ENABLED, false);
                            statuses.put(ComponentStatus.HIDE, true);
                        }
                    }
                    case ENABLE_GUN -> {
                        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
                            statuses.put(ComponentStatus.GUN_ENABLED, true);
                            statuses.put(ComponentStatus.HIDE, false);
                        }
                    }
                    case WEAPON_IS_DROPPED -> {
                        statuses.put(ComponentStatus.IS_PICKED_UP, false);
                        statuses.put(ComponentStatus.HIDE, false);
                        statuses.put(ComponentStatus.GUN_ENABLED, false);
                        statuses.put(ComponentStatus.DROPPED, true);
                        collideBox.setPosition(new Coordinate<>(initialPosition));
                        yOffset = 0;
                        xOffset = 10;
                        direction = true;
                        TimerHandler.get().getTimer("GUN" + getId()).resetTimer();
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        if (component.getGeneralType() == ComponentType.PLAYER) {
            if (!statuses.get(ComponentStatus.IS_PICKED_UP) && !statuses.get(ComponentStatus.DROPPED)) {
                initialPosition = new Coordinate<>(collideBox.getPosition());
                collideBox.setPosition(component.getCollideBox().getPosition());
                yOffset = component.getCollideBox().getHeight() / 2 - 8;
                statuses.put(ComponentStatus.GUN_ENABLED, false);
                statuses.put(ComponentStatus.HIDE, true);
                statuses.put(ComponentStatus.IS_PICKED_UP, true);
                scene.notify(new Message(MessageType.IS_PICKED_UP, gunType, getId()));
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!TimerHandler.get().getTimer("GUN" + getId()).getTimerState()) {
            statuses.put(ComponentStatus.DROPPED, false);
        }
        if (statuses.get(ComponentStatus.NEEDS_RECALIBRATION)) {
            statuses.put(ComponentStatus.NEEDS_RECALIBRATION, false);
            scene.notify(new Message(MessageType.GUN_NEEDS_RECALIBRATION, getGeneralType(), getId()));
        }
        scene.notify(new Message(MessageType.HANDLE_COLLISION, ComponentType.GUN, getId()));
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (!getActiveStatus()) return;
        if (!statuses.get(ComponentStatus.HIDE)) imageWrapper.draw(graphics2D, collideBox, xOffset, yOffset, direction);
    }

    @Override
    public void addMissingPartsAfterDeserialization(Notifiable scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // restore the image
        this.imageWrapper = AssetDeposit.get().getGun(gunType).imageWrapper;

        TimerHandler.get().addTimer(new Timer(0.3f), "GUN" + getId());
        // if in the previous save was in the hands of player,
        // the gun needs recalibration -> his position will be a reference of the player
        statuses.put(ComponentStatus.GUN_ENABLED, false);
        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
            System.out.println("PICKED UP UP AFTER DESERIALIZATION");
            statuses.put(ComponentStatus.NEEDS_RECALIBRATION, true);
            statuses.put(ComponentStatus.HIDE, true);
            scene.notify(new Message(MessageType.IS_PICKED_UP, gunType, getId()));
        }
    }

    @Override
    public ComponentType getCurrentType() {
        return gunType;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.GUN;
    }

    /**
     * Getter for the current image wrapper.
     *
     * @return image wrapper
     */
    public ImageWrapper getImageWrapper() {
        return imageWrapper;
    }
}
