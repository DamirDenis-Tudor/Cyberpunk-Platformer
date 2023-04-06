package Components.DinamicComponents.Items;

import Components.DinamicComponents.DynamicComponent;
import Components.StaticComponents.ImageWrapper;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Gun extends DynamicComponent {
    private final ImageWrapper imageWrapper;

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

    public Gun(Scene scene, Gun gun, Coordinate<Integer> position,ComponentType subType) {
        this.scene = scene;
        this.subType = subType;
        this.imageWrapper = gun.imageWrapper;
        this.collideBox = new Rectangle(gun.collideBox);
        this.collideBox.setPosition(position);
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.IsPickedUp, false);
        statuses.put(ComponentStatus.HasLaunchedBullet, false);
        statuses.put(ComponentStatus.Hide, false);
    }

    @Override
    public void notify(Message message) throws Exception {
        if (message.getSource() == ComponentType.Player) {
            switch (message.getType()) {
                case IsPickedUp -> statuses.put(ComponentStatus.IsPickedUp, true);
                case LaunchBullet -> statuses.put(ComponentStatus.HasLaunchedBullet, true);
                case PlayerDirectionLeft -> {
                    if (statuses.get(ComponentStatus.IsPickedUp)) direction = false;
                }
                case PLayerDirectionRight -> {
                    if (statuses.get(ComponentStatus.IsPickedUp)) direction = true;
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
                        }
                        else {
                            scene.notify(new Message(MessageType.BulletLauchLeft, ComponentType.Gun, getId()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {
        if (component.getBaseType() == ComponentType.Player) {
            if (statuses.get(ComponentStatus.IsPickedUp)) {
                collideBox.setPosition(component.getCollideBox().getPosition());
                xOffset = component.getCollideBox().getWidth() / 2 - collideBox.getWidth() / 2 + 12;
                yOffset = component.getCollideBox().getHeight() / 2 - 2;
            }
        }
    }

    @Override
    public void update() throws Exception {
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Gun, getId()));
    }

    @Override
    public void draw() {
        if (!statuses.get(ComponentStatus.Hide)) imageWrapper.draw(collideBox, xOffset, yOffset, direction);
    }

    @Override
    public ComponentType getSubType() {
        return subType;
    }

    @Override
    public ComponentType getBaseType() {
        return ComponentType.Gun;
    }
}
