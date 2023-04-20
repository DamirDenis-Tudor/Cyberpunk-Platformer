package Components.GameComponents.Characters;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;

import java.util.Map;

import static Utils.Constants.enemyRange;
import static Utils.Constants.gravitationForce;

/**
 * This class describes a basic enemy behavior.The code might be complicated, but it is not.
 * It is nothing more than a state machine that describes the interactions with other components.
 */
public class Enemy extends DynamicComponent {
    transient protected AnimationHandler animationHandler;
    transient protected TimersHandler timersHandler;
    protected Map<ComponentStatus, Boolean> statuses;
    protected final Map<GeneralAnimationTypes, AnimationType> animationsType;
    protected int health = 100;
    protected int velocity = 0;

    public Enemy(Scene scene, Coordinate<Integer> position, ComponentType type){
        super();
        this.scene = scene;
        animationHandler = new AnimationHandler();

        timersHandler = TimersHandler.get();
        timersHandler.addTimer(new Timer(0.2f), TimerType.LockTarget.toString() + getId());

        subtype = type;
        velocity = CharacterisesGenerator.getVelocityFor(type);
        statuses = CharacterisesGenerator.generateStatusesFor(ComponentType.Enemy);
        animationsType = CharacterisesGenerator.generateAnimationTypesFor(type, getId());

        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Idle), new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case Map -> {
                switch (message.type()) {
                    case LeftCollision -> {
                        statuses.put(ComponentStatus.LeftCollision, true);
                        statuses.put(ComponentStatus.RightCollision, false);
                    }
                    case RightCollision -> {
                        statuses.put(ComponentStatus.LeftCollision, false);
                        statuses.put(ComponentStatus.RightCollision, true);
                    }
                    case ActivateBottomCollision -> statuses.put(ComponentStatus.BottomCollision, true);

                }
            }
            case Enemy -> {
                switch (message.type()) {
                    case LeftCollisionWithOther -> {
                        statuses.put(ComponentStatus.LeftCollision, true);
                        statuses.put(ComponentStatus.RightCollision, false);
                        statuses.put(ComponentStatus.HasEnemyCollision, true);
                    }
                    case RightCollisionWithOther -> {
                        statuses.put(ComponentStatus.LeftCollision, false);
                        statuses.put(ComponentStatus.RightCollision, true);
                        statuses.put(ComponentStatus.HasEnemyCollision, true);
                    }
                }
            }
            case Player, Bullet -> {
                switch (message.type()) {
                    case Attack, HasCollision -> {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Hurt), collideBox.getPosition());
                        animationHandler.getAnimation().setRepeats(4);
                        statuses.put(ComponentStatus.Hurt, true);
                        health -= 25;
                        if (health <= 0) {
                            statuses.put(ComponentStatus.Death, true);
                            collideBox.getPosition().setX(-1);
                            collideBox.getPosition().setY(-1);
                            setActiveStatus(false);
                            scene.notify(new Message(MessageType.Destroy, ComponentType.Enemy, getId()));
                        }
                    }
                    case PlayerDeath -> {
                        statuses.put(ComponentStatus.Idle, false);
                        statuses.put(ComponentStatus.Attack, false);
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent)object;
        switch (component.getGeneralType()) {
            case Player -> {
                if (collideBox.intersects(component.getCollideBox()) && !statuses.get(ComponentStatus.Hurt)) {
                    statuses.put(ComponentStatus.Attack, true);
                    if (!timersHandler.getTimer(TimerType.LockTarget.toString() + getId()).getTimerState() && !statuses.get(ComponentStatus.FirstHit)) {
                        statuses.put(ComponentStatus.FirstHit, true);
                        component.notify(new Message(MessageType.Attack, ComponentType.Enemy, getId()));
                    }
                } else if (statuses.get(ComponentStatus.Attack) && animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.Attack, false);
                }

                if ((!statuses.get(ComponentStatus.HasEnemyCollision) || subtype == ComponentType.GunnerEnemy || subtype == ComponentType.MachineGunEnemy) &&
                        collideBox.getMinY() < component.getCollideBox().getCenterY() &&
                        collideBox.getMaxY() > component.getCollideBox().getCenterY() &&
                        collideBox.calculateDistanceWith(component.getCollideBox()) < enemyRange) {
                    statuses.put(ComponentStatus.HasDetectedPLayer, true);
                    if (collideBox.getCenterX() - component.getCollideBox().getCenterX() > 0) {
                        if (!animationHandler.getAnimation().getDirection() &&
                                statuses.get(ComponentStatus.LeftCollision)) {
                            if (subtype == ComponentType.GunnerEnemy || subtype == ComponentType.MachineGunEnemy) {
                                statuses.put(ComponentStatus.Attack, true);
                            } else {
                                statuses.put(ComponentStatus.Idle, true);
                            }
                        } else {
                            statuses.put(ComponentStatus.RightCollision, true);
                            statuses.put(ComponentStatus.LeftCollision, false);
                        }
                    } else if (collideBox.getCenterX() - component.getCollideBox().getCenterX() < 0) {
                        if (animationHandler.getAnimation().getDirection() &&
                                statuses.get(ComponentStatus.RightCollision)) {
                            if (subtype == ComponentType.GunnerEnemy || subtype == ComponentType.MachineGunEnemy) {
                                statuses.put(ComponentStatus.Attack, true);
                            } else {
                                statuses.put(ComponentStatus.Idle, true);
                            }
                        } else {
                            statuses.put(ComponentStatus.RightCollision, false);
                            statuses.put(ComponentStatus.LeftCollision, true);
                        }
                    } else {
                        statuses.put(ComponentStatus.HasDetectedPLayer, false);
                    }
                } else {
                    statuses.put(ComponentStatus.Idle, false);
                    statuses.put(ComponentStatus.HasDetectedPLayer, false);
                }
            }
            case Enemy -> {
                if (collideBox.intersects(component.getCollideBox())) {
                    collideBox.solveCollision(component.getCollideBox());
                    if (collideBox.getDx() > 0) {
                        component.notify(new Message(MessageType.RightCollisionWithOther, ComponentType.Enemy, getId()));
                        if (!(statuses.get(ComponentStatus.HasDetectedPLayer))) {
                            statuses.put(ComponentStatus.LeftCollision, true);
                            statuses.put(ComponentStatus.RightCollision, false);
                            statuses.put(ComponentStatus.HasEnemyCollision, true);
                        }
                    } else if (collideBox.getDx() < 0) {
                        component.notify(new Message(MessageType.LeftCollisionWithOther, ComponentType.Enemy, getId()));
                        if (!(statuses.get(ComponentStatus.HasDetectedPLayer))) {
                            statuses.put(ComponentStatus.RightCollision, true);
                            statuses.put(ComponentStatus.LeftCollision, false);
                            statuses.put(ComponentStatus.HasEnemyCollision, true);
                        }
                    }
                } else {
                    statuses.put(ComponentStatus.HasEnemyCollision, false);
                }
            }
        }
    }

    protected void handleAnimations(){
        if (statuses.get(ComponentStatus.Death)) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Death), collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.Hurt)) {
                if (animationHandler.getAnimation().repeatsAreOver()) {
                    statuses.put(ComponentStatus.Hurt, false);
                }
            } else if (statuses.get(ComponentStatus.Attack)) {
                if (subtype == ComponentType.GunnerEnemy || subtype == ComponentType.MachineGunEnemy) {
                    if (!timersHandler.getTimer(subtype.toString() + getId()).getTimerState()) {
                        timersHandler.getTimer(subtype.toString() + getId()).resetTimer();
                        if (animationHandler.getAnimation().getDirection()) {
                            scene.notify(new Message(MessageType.BulletLaunchRight, ComponentType.Enemy, getId()));
                        } else {
                            scene.notify(new Message(MessageType.BulletLaunchLeft, ComponentType.Enemy, getId()));
                        }
                    }
                }
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Attack), collideBox.getPosition());
                if (animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.FirstHit, false);
                }
            } else if (statuses.get(ComponentStatus.Idle)) {
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Idle), collideBox.getPosition());
            } else if (statuses.get(ComponentStatus.HorizontalMove)) {
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Walk), collideBox.getPosition());
            }
        }
    }

    @Override
    public void update(){
        if (!statuses.get(ComponentStatus.BottomCollision)) {
            collideBox.moveByY(gravitationForce);
        }
        if (!statuses.get(ComponentStatus.Hurt) &&
                !statuses.get(ComponentStatus.Attack) &&
                !statuses.get(ComponentStatus.Idle)) {
            if (!statuses.get(ComponentStatus.LeftCollision)) {
                statuses.put(ComponentStatus.HorizontalMove, true);
                animationHandler.getAnimation().setDirection(false);
            } else if (!statuses.get(ComponentStatus.RightCollision)) {
                statuses.put(ComponentStatus.HorizontalMove, true);
                animationHandler.getAnimation().setDirection(true);
            }
        } else {
            statuses.put(ComponentStatus.HorizontalMove, false);
        }

        if (statuses.get(ComponentStatus.HorizontalMove)) {
            if (statuses.get(ComponentStatus.HasDetectedPLayer) &&
                    (subtype == ComponentType.GunnerEnemy || subtype == ComponentType.MachineGunEnemy)) {
                statuses.put(ComponentStatus.HorizontalMove, false);
                statuses.put(ComponentStatus.Attack, true);
            } else {
                if (animationHandler.getAnimation().getDirection()) {
                    collideBox.moveByX(velocity);
                } else {
                    collideBox.moveByX(-velocity);
                }
            }
        }

        handleAnimations();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Enemy, getId()));
        animationHandler.update();
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentType getCurrentType() {
        return subtype;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.Enemy;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);

        timersHandler = TimersHandler.get();
        timersHandler.addTimer(new Timer(0.2f), TimerType.LockTarget.toString() + getId());

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Walk), collideBox.getPosition());

        collideBox = animationHandler.getAnimation().getRectangle();
        if (statuses.get(ComponentStatus.LeftCollision)){
            animationHandler.getAnimation().setDirection(true);
        }else if (statuses.get(ComponentStatus.RightCollision)){
            animationHandler.getAnimation().setDirection(false);
        }

        switch (subtype){
            case GunnerEnemy -> TimersHandler.get().addTimer(new Timer(0.5f) , subtype.name()+getId());
            case MachineGunEnemy -> TimersHandler.get().addTimer(new Timer(0.2f) , subtype.name()+getId());
        }
    }
}
