package Components.DinamicComponents.Characters.Enemies;

import Components.DinamicComponents.Characters.AnimationHandler;
import Components.DinamicComponents.DinamicComponent;
import Enums.AnimationType;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class BasicEnemy extends DinamicComponent {
    private final AnimationHandler animationHandler;
    private final TimersHandler timersHandler;
    private final Map<ComponentStatus, Boolean> statuses;
    private int health = 100;

    public BasicEnemy(Scene scene, Coordinate<Integer> position) throws Exception {

        this.scene = scene;
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Enemy1Idle, position);
        collideBox = animationHandler.getAnimation().getRectangle();

        timersHandler = TimersHandler.getInstance();
        timersHandler.addTimer(new Timer(1f), "stan" + getId());
        timersHandler.addTimer(new Timer(0.2f), "lock_target" + getId());

        statuses = new HashMap<>();
        statuses.put(ComponentStatus.BottomCollision, false);
        statuses.put(ComponentStatus.LeftCollision, false);
        statuses.put(ComponentStatus.RightCollision, false);
        statuses.put(ComponentStatus.LeftCollisionWithOther, false);
        statuses.put(ComponentStatus.RightCollisionWithOther, false);
        statuses.put(ComponentStatus.HorizontalMove, false);
        statuses.put(ComponentStatus.Hurt, false);
        statuses.put(ComponentStatus.Death, false);
        statuses.put(ComponentStatus.FirstHit, false);
        statuses.put(ComponentStatus.Attack, false);
        statuses.put(ComponentStatus.HasDetectedPlayer, false);
        statuses.put(ComponentStatus.HasEnemyCollision, false);
        statuses.put(ComponentStatus.Idle, false);
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.getSource()) {
            case Map -> {
                switch (message.getType()) {
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
            case BasicEnemy -> {
                switch (message.getType()) {
                    case LeftCollisionWithOther -> {
                        statuses.put(ComponentStatus.LeftCollisionWithOther, true);
                        statuses.put(ComponentStatus.RightCollisionWithOther, false);
                        statuses.put(ComponentStatus.LeftCollision, true);
                        statuses.put(ComponentStatus.RightCollision, false);
                        statuses.put(ComponentStatus.HasEnemyCollision, true);
                    }
                    case RightCollisionWithOther -> {
                        statuses.put(ComponentStatus.RightCollisionWithOther, true);
                        statuses.put(ComponentStatus.LeftCollisionWithOther, false);
                        statuses.put(ComponentStatus.LeftCollision, false);
                        statuses.put(ComponentStatus.RightCollision, true);
                        statuses.put(ComponentStatus.HasEnemyCollision, true);
                    }
                    case EnemyDeath -> statuses.put(ComponentStatus.HasEnemyCollision, false);
                }
            }
            case Player -> {
                switch (message.getType()) {
                    case Attack -> {
                        animationHandler.changeAnimation(AnimationType.Enemy1Hurt, collideBox.getPosition());
                        animationHandler.getAnimation().setRepeats(4);
                        statuses.put(ComponentStatus.Hurt, true);
                        health -= 40;
                        if (health <= 0) {
                            statuses.put(ComponentStatus.Death, true);
                            scene.notify(new Message(MessageType.EnemyDeath, ComponentType.BasicEnemy));
                            setActiveStatus(false);
                        }
                    }
                    case PlayerDeath -> {
                        statuses.put(ComponentStatus.HasDetectedPlayer, false);
                        statuses.put(ComponentStatus.Idle, false);
                        statuses.put(ComponentStatus.Attack, false);
                    }
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) throws Exception {
        switch (component.getType()) {
            case Player -> {
                statuses.put(ComponentStatus.HasDetectedPlayer, true);
                if (collideBox.intersects(component.getCollideBox()) && !statuses.get(ComponentStatus.Hurt)) {
                    statuses.put(ComponentStatus.Attack, true);
                    if (!timersHandler.getTimer("lock_target" + getId()).getTimerState() && !statuses.get(ComponentStatus.FirstHit)) {
                        statuses.put(ComponentStatus.FirstHit, true);
                        component.notify(new Message(MessageType.Attack, ComponentType.BasicEnemy));
                    }
                } else if (collideBox.getMinY() < component.getCollideBox().getCenterY() && collideBox.getMaxY() > component.getCollideBox().getCenterY()) {
                    if (collideBox.getCenterX() - component.getCollideBox().getCenterX() > 0) {
                        if (!animationHandler.getAnimation().getDirection() &&
                                statuses.get(ComponentStatus.LeftCollision) &&
                                !statuses.get(ComponentStatus.LeftCollisionWithOther)) {
                            statuses.put(ComponentStatus.Idle, true);
                        } else {
                            statuses.put(ComponentStatus.RightCollision, true);
                            statuses.put(ComponentStatus.LeftCollision, false);
                        }
                    } else if (collideBox.getCenterX() - component.getCollideBox().getCenterX() < 0) {
                        if (animationHandler.getAnimation().getDirection() &&
                                statuses.get(ComponentStatus.RightCollision) &&
                                !statuses.get(ComponentStatus.RightCollisionWithOther)) {
                            statuses.put(ComponentStatus.Idle, true);
                        } else {
                            statuses.put(ComponentStatus.RightCollision, false);
                            statuses.put(ComponentStatus.LeftCollision, true);
                        }
                    } else {
                        statuses.put(ComponentStatus.Idle, false);
                    }
                }
                if (statuses.get(ComponentStatus.Attack) && animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.Attack, false);
                }
            }
            case BasicEnemy -> {
                collideBox.solveCollision(component.getCollideBox());
                if (collideBox.getDx() > 0) {
                    component.notify(new Message(MessageType.RightCollisionWithOther, ComponentType.BasicEnemy));
                    statuses.put(ComponentStatus.LeftCollision, true);
                    statuses.put(ComponentStatus.RightCollision, false);
                } else if (collideBox.getDx() < 0) {
                    component.notify(new Message(MessageType.LeftCollisionWithOther, ComponentType.BasicEnemy));
                    statuses.put(ComponentStatus.RightCollision, true);
                    statuses.put(ComponentStatus.LeftCollision, false);
                }
            }
        }
    }

    @Override
    public void update() throws Exception {
        if (!statuses.get(ComponentStatus.BottomCollision)) {
            collideBox.moveByY(8);
        }
        if (!statuses.get(ComponentStatus.Attack) &&
                !statuses.get(ComponentStatus.Hurt) &&
                !statuses.get(ComponentStatus.Idle)) {

            if (!statuses.get(ComponentStatus.LeftCollision)) {
                statuses.put(ComponentStatus.HorizontalMove, true);
                animationHandler.getAnimation().setDirection(false);
                collideBox.moveByX(-1);
            } else if (!statuses.get(ComponentStatus.RightCollision)) {
                statuses.put(ComponentStatus.HorizontalMove, true);
                animationHandler.getAnimation().setDirection(true);
                collideBox.moveByX(1);
            }
        } else {
            statuses.put(ComponentStatus.HorizontalMove, false);
        }

        if (statuses.get(ComponentStatus.Death)) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(AnimationType.Enemy1Death, collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.Hurt)) {
                if (animationHandler.getAnimation().repeatsAreOver()) {
                    statuses.put(ComponentStatus.Hurt, false);
                }
            } else if (statuses.get(ComponentStatus.Attack)) {
                animationHandler.changeAnimation(AnimationType.Enemy1Attack, collideBox.getPosition());
                if (animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.FirstHit, false);
                }
            } else if (statuses.get(ComponentStatus.Idle)) {
                animationHandler.changeAnimation(AnimationType.Enemy1Idle, collideBox.getPosition());
            } else if (statuses.get(ComponentStatus.HorizontalMove)) {
                animationHandler.changeAnimation(AnimationType.Enemy1Walk, collideBox.getPosition());
            }
        }
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.BasicEnemy));
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentType getType() {
        return ComponentType.BasicEnemy;
    }

}
