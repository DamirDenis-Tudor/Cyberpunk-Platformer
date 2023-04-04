package Components.DinamicComponents.Characters.Enemies;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.AnimationHandler;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.TimersHandler;
import Utils.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class AnimalEnemy extends DinamicComponent {
    private final TimersHandler timersHandler;
    private final AnimationHandler animationHandler;
    private final Map<ComponentStatus, Boolean> statuses;
    private final Map<GeneralAnimationTypes , AnimationType> animationsType;
    private int health = 100;

    public AnimalEnemy(Scene scene, Coordinate<Integer> position , ComponentType type) throws Exception {
        this.scene = scene;

        timersHandler = TimersHandler.getInstance();

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.Dog1Idle, position);
        collideBox = animationHandler.getAnimation().getRectangle();

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
        statuses.put(ComponentStatus.HasEnemyCollision, false);

        animationsType = new HashMap<>();
        switch (type){
            case Dog1 -> {
                animationsType.put(GeneralAnimationTypes.Idle , AnimationType.Dog1Idle);
                animationsType.put(GeneralAnimationTypes.Walk , AnimationType.Dog1Walk);
                animationsType.put(GeneralAnimationTypes.Attack , AnimationType.Dog1Attack);
                animationsType.put(GeneralAnimationTypes.Hurt , AnimationType.Dog1Hurt);
                animationsType.put(GeneralAnimationTypes.Death , AnimationType.Dog1Death);
            }
            case Dog2 ->{
                animationsType.put(GeneralAnimationTypes.Idle , AnimationType.Dog2Idle);
                animationsType.put(GeneralAnimationTypes.Walk , AnimationType.Dog2Walk);
                animationsType.put(GeneralAnimationTypes.Attack , AnimationType.Dog2Attack);
                animationsType.put(GeneralAnimationTypes.Hurt , AnimationType.Dog2Hurt);
                animationsType.put(GeneralAnimationTypes.Death , AnimationType.Dog2Death);
            }
            case Cat1 -> {
                animationsType.put(GeneralAnimationTypes.Idle , AnimationType.Cat1Idle);
                animationsType.put(GeneralAnimationTypes.Walk , AnimationType.Cat1Walk);
                animationsType.put(GeneralAnimationTypes.Attack , AnimationType.Cat1Attack);
                animationsType.put(GeneralAnimationTypes.Hurt , AnimationType.Cat1Hurt);
                animationsType.put(GeneralAnimationTypes.Death , AnimationType.Cat1Death);
            }
            case Cat2 -> {
                animationsType.put(GeneralAnimationTypes.Idle , AnimationType.Cat2Idle);
                animationsType.put(GeneralAnimationTypes.Walk , AnimationType.Cat2Walk);
                animationsType.put(GeneralAnimationTypes.Attack , AnimationType.Cat2Attack);
                animationsType.put(GeneralAnimationTypes.Hurt , AnimationType.Cat2Hurt);
                animationsType.put(GeneralAnimationTypes.Death , AnimationType.Cat2Death);
            }
        }

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
            case Player -> {
                switch (message.getType()) {
                    case Attack -> {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Hurt), collideBox.getPosition());
                        animationHandler.getAnimation().setRepeats(4);
                        statuses.put(ComponentStatus.Hurt, true);
                        health -= 40;
                        if (health <= 0) {
                            statuses.put(ComponentStatus.Death, true);
                            scene.notify(new Message(MessageType.EnemyDeath, ComponentType.BaseballEnemy));
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
            case BaseballEnemy, SkaterEnemy, AnimalEnemy -> {
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
        }
    }
    @Override
    public void handleInteractionWith(DinamicComponent component) throws Exception {
        switch (component.getType()) {
            case Player -> {
                if (collideBox.intersects(component.getCollideBox()) && !statuses.get(ComponentStatus.Hurt)) {
                    statuses.put(ComponentStatus.Attack, true);
                    if (!statuses.get(ComponentStatus.FirstHit)) {
                        statuses.put(ComponentStatus.FirstHit, true);
                        component.notify(new Message(MessageType.Attack, ComponentType.SkaterEnemy));
                    }
                } else if (statuses.get(ComponentStatus.Attack) && animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.Attack, false);
                }
            }
            case BaseballEnemy, SkaterEnemy, AnimalEnemy -> {
                collideBox.solveCollision(component.getCollideBox());
                if (collideBox.getDx() > 0) {
                    component.notify(new Message(MessageType.RightCollisionWithOther, ComponentType.BaseballEnemy));
                    statuses.put(ComponentStatus.LeftCollision, true);
                    statuses.put(ComponentStatus.RightCollision, false);
                    statuses.put(ComponentStatus.HasEnemyCollision, true);
                } else if (collideBox.getDx() < 0) {
                    component.notify(new Message(MessageType.LeftCollisionWithOther, ComponentType.BaseballEnemy));
                    statuses.put(ComponentStatus.RightCollision, true);
                    statuses.put(ComponentStatus.LeftCollision, false);
                    statuses.put(ComponentStatus.HasEnemyCollision, true);
                }
            }
        }
    }

    @Override
    public void update() throws Exception {
        if (!statuses.get(ComponentStatus.BottomCollision)) {
            collideBox.moveByY(8);
        }
        if (!statuses.get(ComponentStatus.Hurt) && !statuses.get(ComponentStatus.Attack)) {
            if (!statuses.get(ComponentStatus.LeftCollision)) {
                statuses.put(ComponentStatus.HorizontalMove, true);
                animationHandler.getAnimation().setDirection(false);
                collideBox.moveByX(-2);
            } else if (!statuses.get(ComponentStatus.RightCollision)) {
                statuses.put(ComponentStatus.HorizontalMove, true);
                animationHandler.getAnimation().setDirection(true);
                collideBox.moveByX(2);
            }
        } else {
            statuses.put(ComponentStatus.HorizontalMove, false);
        }

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
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Attack), collideBox.getPosition());
                if (animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.FirstHit, false);
                }
            } else if (statuses.get(ComponentStatus.HorizontalMove)) {
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Walk), collideBox.getPosition());
            }
        }
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.AnimalEnemy));
        animationHandler.update();
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentType getType() {
        return ComponentType.AnimalEnemy;
    }
}

