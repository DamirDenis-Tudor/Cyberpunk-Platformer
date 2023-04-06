package Components.DinamicComponents;

import Components.StaticComponents.AnimationHandler;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.TimersHandler;
import Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.platformVelocity;

public class Platform extends DynamicComponent {
    protected final AnimationHandler animationHandler;
    protected final Map<ComponentStatus, Boolean> statuses;
    public Platform(Scene scene, Coordinate<Integer> position) throws Exception {
        super();
        this.scene = scene;

        statuses = new HashMap<>();
        statuses.put(ComponentStatus.LeftCollision, false);
        statuses.put(ComponentStatus.RightCollision, false);

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Platform, position);
        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) throws Exception {
        if(message.source() == ComponentType.Map){
            switch (message.type()){
                case LeftCollision -> {
                    statuses.put(ComponentStatus.LeftCollision,true);
                    statuses.put(ComponentStatus.RightCollision,false);
                    animationHandler.getAnimation().setDirection(true);
                }
                case RightCollision -> {
                    statuses.put(ComponentStatus.RightCollision,true);
                    statuses.put(ComponentStatus.LeftCollision,false);
                    animationHandler.getAnimation().setDirection(false);
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {
        if(component.getBaseType() == ComponentType.Player){
            if (animationHandler.getAnimation().getDirection()) {
                component.getCollideBox().moveByX(platformVelocity);
            } else {
                component.getCollideBox().moveByX(-platformVelocity);
            }
        }
    }

    @Override
    public void update() throws Exception {
        if(!statuses.get(ComponentStatus.LeftCollision)){
            collideBox.moveByX(-platformVelocity);
        } else if (!statuses.get(ComponentStatus.RightCollision)) {
            collideBox.moveByX(platformVelocity);
        }
        scene.notify(new Message(MessageType.HandleCollision , ComponentType.Platform , getId()));
        animationHandler.update();
    }

    @Override
    public void draw() {animationHandler.draw();}

    @Override
    public ComponentType getSubType() {
        return null;
    }

    @Override
    public ComponentType getBaseType() {
        return ComponentType.Platform;
    }
}
