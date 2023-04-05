package Components.DinamicComponents.Items;

import Components.DinamicComponents.DynamicComponent;
import Components.DinamicComponents.Player;
import Components.StaticComponents.ImageWrapper;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.GameWindow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Gun extends DynamicComponent {
    private final ImageWrapper imageWrapper;

    private int xOffset;
    private int yOffset;
    private final Map<ComponentStatus, Boolean> statuses;

    public Gun(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
        statuses = new HashMap<>();
    }

    public Gun(Scene scene, Gun gun , Coordinate<Integer> position){
        this.scene = scene;
        this.imageWrapper = gun.imageWrapper;
        this.collideBox = new Rectangle(gun.collideBox);
        this.collideBox.setPosition(position);
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.IsPickedUp , false);
        statuses.put(ComponentStatus.HasLaunchedBullet , false);
        statuses.put(ComponentStatus.Hide , false);
    }

    @Override
    public void notify(Message message) throws Exception {
        if(message.getSource() == ComponentType.Player){
            switch (message.getType()){
                case IsPickedUp -> statuses.put(ComponentStatus.IsPickedUp,true);
                case LaunchBullet -> statuses.put(ComponentStatus.HasLaunchedBullet,true);
            }
        }
    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {
        if(component.getType() == ComponentType.Player){
            if(statuses.get(ComponentStatus.IsPickedUp)){
                collideBox.setPosition(component.getCollideBox().getPosition());
                xOffset = component.getCollideBox().getWidth()/2 - collideBox.getWidth()/2 + 5;
                yOffset = component.getCollideBox().getHeight()/2 + 2;
                statuses.get(ComponentStatus.IsPickedUp);
            }
        }
    }

    @Override
    public void update() throws Exception {
        if(statuses.get(ComponentStatus.IsPickedUp) && statuses.get(ComponentStatus.HasLaunchedBullet)){
            scene.notify(new Message(MessageType.LaunchBullet , ComponentType.Gun , getId()));
        }
        scene.notify(new Message(MessageType.HandleCollision , ComponentType.Gun , getId()));
    }

    @Override
    public void draw() {
        imageWrapper.draw(collideBox,xOffset,yOffset);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.Gun;
    }
}
