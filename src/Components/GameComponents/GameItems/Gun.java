package Components.GameComponents.GameItems;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.GameComponents.CharacterisesGenerator;
import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Enums.ComponentType.SCENE;

public class Gun extends DynamicComponent {
    transient private ImageWrapper imageWrapper;
    private boolean direction = true; // right - true,left - false
    private ComponentType subType = null;
    private int xOffset = 10;
    private int yOffset = 0;
    private final Map<ComponentStatus, Boolean> statuses;

    public Gun(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
        statuses = new HashMap<>();
    }

    public Gun(Scene scene, Coordinate<Integer> position, ComponentType subType) {
        this.scene = scene;
        this.subType = subType;
        this.imageWrapper = AssetsDeposit.get().getGun(subType).imageWrapper;
        this.collideBox = new Rectangle(AssetsDeposit.get().getGun(subType).collideBox);
        this.collideBox.setPosition(new Coordinate<>(position));
        statuses = CharacterisesGenerator.generateStatusesFor(ComponentType.GUN);

    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case PLAYER -> {
                switch (message.type()) {
                    case LAUNCH_BULLET -> {
                        if (statuses.get(ComponentStatus.GUN_ENABLED)) statuses.put(ComponentStatus.HAS_LAUNCHED_BULLET, true);
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
                        if (statuses.get(ComponentStatus.IS_PICKED_UP)&& statuses.get(ComponentStatus.GUN_ENABLED)) statuses.put(ComponentStatus.HIDE, true);
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
                        System.out.println("DISABLE GUN");
                        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
                            statuses.put(ComponentStatus.GUN_ENABLED, false);
                            statuses.put(ComponentStatus.HIDE, true);
                        }
                    }
                    case ENABLE_GUN -> {
                        System.out.println("ENABLE GUN");
                        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
                            statuses.put(ComponentStatus.GUN_ENABLED, true);
                            statuses.put(ComponentStatus.HIDE, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        if (component.getGeneralType() == ComponentType.PLAYER) {
            if (!statuses.get(ComponentStatus.IS_PICKED_UP)) {
                statuses.put(ComponentStatus.GUN_ENABLED , false);
                statuses.put(ComponentStatus.HIDE , true);
                statuses.put(ComponentStatus.IS_PICKED_UP, true);
                scene.notify(new Message(MessageType.IS_PICKED_UP, subType, getId()));
                collideBox.setPosition(component.getCollideBox().getPosition());
                yOffset = component.getCollideBox().getHeight() / 2 - 8;
            }
        }
    }

    @Override
    public void update() {
        super.update();
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
    public ComponentType getCurrentType() {
        return subType;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.GUN;
    }

    public ImageWrapper getImageWrapper() {
        return imageWrapper;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Notifiable scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // restore the image
        this.imageWrapper = AssetsDeposit.get().getGun(subType).imageWrapper;

        // if in the previous save was in the hands of player,
        // the gun needs recalibration -> his position will be a reference of the player
        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
            statuses.put(ComponentStatus.NEEDS_RECALIBRATION, true);
        }

        statuses.put(ComponentStatus.GUN_ENABLED, false);
        statuses.put(ComponentStatus.HIDE, true);
        if (statuses.get(ComponentStatus.IS_PICKED_UP)) {
            System.out.println("PICKED UP UP AFTER DESERIALIZATION");
            scene.notify(new Message(MessageType.IS_PICKED_UP, subType, getId()));
        }
    }
}
