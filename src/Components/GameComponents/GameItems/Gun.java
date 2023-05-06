package Components.GameComponents.GameItems;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.GameComponents.DynamicComponent;
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

    public Gun(Scene scene, Coordinate<Integer> position,ComponentType subType) {
        this.scene = scene;
        this.subType = subType;
        this.imageWrapper = AssetsDeposit.get().getGun(subType).imageWrapper;
        this.collideBox = new Rectangle(AssetsDeposit.get().getGun(subType).collideBox);
        this.collideBox.setPosition(new Coordinate<>(position));
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.IsPickedUp, false);
        statuses.put(ComponentStatus.HasLaunchedBullet, false);
        statuses.put(ComponentStatus.Hide, false);
        statuses.put(ComponentStatus.NeedsRecalibration , false);
    }

    @Override
    public void notify(Message message) {
        if (message.source() == ComponentType.Player) {
            switch (message.type()) {
                case IsPickedUp -> statuses.put(ComponentStatus.IsPickedUp, true);
                case LaunchBullet -> statuses.put(ComponentStatus.HasLaunchedBullet, true);
                case PlayerDirectionLeft -> {
                    if (statuses.get(ComponentStatus.IsPickedUp)) {
                        direction = false;
                        xOffset = 35;
                    }
                }
                case PLayerDirectionRight -> {
                    if (statuses.get(ComponentStatus.IsPickedUp)) {
                        direction = true;
                        xOffset = 27;
                    }
                }
                case HideGun -> {
                    if (statuses.get(ComponentStatus.IsPickedUp)) statuses.put(ComponentStatus.Hide, true);
                }
                case ShowGun -> {
                    if (statuses.get(ComponentStatus.IsPickedUp)) statuses.put(ComponentStatus.Hide, false);
                }
                case Shoot -> {
                    if (statuses.get(ComponentStatus.IsPickedUp)){
                        if(direction){
                            scene.notify(new Message(MessageType.BulletLaunchRight, ComponentType.Gun, getId()));
                        } else {
                            scene.notify(new Message(MessageType.BulletLaunchLeft, ComponentType.Gun, getId()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        if (component.getGeneralType() == ComponentType.Player) {
            if (statuses.get(ComponentStatus.IsPickedUp)) {
                collideBox.setPosition(component.getCollideBox().getPosition());
                yOffset = component.getCollideBox().getHeight() / 2 - 8;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if(statuses.get(ComponentStatus.NeedsRecalibration)){
            statuses.put(ComponentStatus.NeedsRecalibration , false);
            scene.notify(new Message(MessageType.GunNeedsRecalibration, getGeneralType() , getId()));
        }
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Gun, getId()));
    }
    @Override
    public void draw(Graphics2D graphics2D) {
        if(!getActiveStatus()) return;
        if (!statuses.get(ComponentStatus.Hide)) imageWrapper.draw(graphics2D,collideBox, xOffset, yOffset, direction);
    }
    @Override
    public ComponentType getCurrentType() {
        return subType;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.Gun;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // restore the image
        this.imageWrapper = AssetsDeposit.get().getGun(subType).imageWrapper;

        // if in the previous save was in the hands of player,
        // the gun needs recalibration -> his position will be a reference of the player
        if(statuses.get(ComponentStatus.IsPickedUp)){
            statuses.put(ComponentStatus.NeedsRecalibration , true);
        }
    }
}
