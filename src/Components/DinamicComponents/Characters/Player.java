package Components.DinamicComponents.Characters;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.AnimationHandler;
import Enums.AnimationType;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Input.KeyboardInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Window.Camera;

import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.velocity;

public class Player extends DinamicComponent{
    private final KeyboardInput keyboardInput;
    private final Camera camera;
    private final TimersHandler timersHandler;
    private final AnimationHandler animationHandler;
    private final Map<ComponentStatus, Boolean> statuses;
    private int health = 100;
    private Integer jumpsCounter = 0;
    private final AnimationType[] attackCombo;
    private int attackComboIndex = 0;

    public Player(Scene scene, Coordinate<Integer> position) throws Exception {
        this.scene = scene;

        keyboardInput = KeyboardInput.getInstance();

        timersHandler = TimersHandler.getInstance();
        timersHandler.addTimer(new Timer(0.30f), getType().name());

        camera = Camera.getInstance();
        camera.setPastTargetPosition(position.getPosX());

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.BikerIdle, position);
        collideBox = animationHandler.getAnimation().getRectangle();

        attackCombo = new AnimationType[3];
        attackCombo[0] = AnimationType.BikerAttack1;
        attackCombo[1] = AnimationType.BikerAttack2;
        attackCombo[2] = AnimationType.BikerAttack3;

        statuses = new HashMap<>();
        statuses.put(ComponentStatus.BottomCollision, false);
        statuses.put(ComponentStatus.TopCollision, false);
        statuses.put(ComponentStatus.IsOnLadder , false);
        statuses.put(ComponentStatus.HorizontalMove, false);
        statuses.put(ComponentStatus.Hurt, false);
        statuses.put(ComponentStatus.Death, false);
        statuses.put(ComponentStatus.FirstHit, false);
        statuses.put(ComponentStatus.Attack, false);
        statuses.put(ComponentStatus.IsMovingOnLadder, false);
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.getSource()) {
            case Map -> {
                switch (message.getType()){
                    case ActivateBottomCollision -> {
                        statuses.put(ComponentStatus.BottomCollision, true);
                        statuses.put(ComponentStatus.TopCollision, false);
                        jumpsCounter = 0;
                    }
                    case DeactivateBottomCollision -> statuses.put(ComponentStatus.BottomCollision, false);
                    case ActivateTopCollision -> statuses.put(ComponentStatus.TopCollision, true);
                    case IsOnLadder -> {
                        statuses.put(ComponentStatus.IsOnLadder , true);
                        jumpsCounter = 1;
                    }
                    case IsNoLongerOnLadder -> statuses.put(ComponentStatus.IsOnLadder , false);
                }
            }
            case BaseballEnemy, SkaterEnemy, AnimalEnemy-> {
                if (message.getType() == MessageType.Attack && !statuses.get(ComponentStatus.Attack)) {
                    animationHandler.changeAnimation(AnimationType.BikerHurt, collideBox.getPosition());
                    animationHandler.getAnimation().setRepeats(2);
                    statuses.put(ComponentStatus.Hurt, true);
                    health -= 1;
                    if (health <= 0) {
                        statuses.put(ComponentStatus.Death, true);
                        setActiveStatus(false);
                        scene.notify(new Message(MessageType.PlayerDeath, ComponentType.Player));
                    }
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) throws Exception {
        switch (component.getType()) {
            case Map -> {

            }
            case BaseballEnemy, SkaterEnemy,AnimalEnemy -> {
                if (collideBox.intersects(component.getCollideBox()) &&
                        statuses.get(ComponentStatus.Attack) &&
                        !statuses.get(ComponentStatus.FirstHit)) {
                    component.notify(new Message(MessageType.Attack, ComponentType.Player));
                    statuses.put(ComponentStatus.FirstHit, true);
                }
            }
        }
    }

    @Override
    public void update() throws Exception {
        // horizontal movement
        statuses.put(ComponentStatus.HorizontalMove, false);
        if ((!statuses.get(ComponentStatus.Attack)) || jumpsCounter != 0) {
            if (keyboardInput.getKeyD()) {
                animationHandler.getAnimation().setDirection(true);
                if(statuses.get(ComponentStatus.IsOnLadder)){
                    collideBox.moveByX(5);
                }else {
                    collideBox.moveByX(5);
                }
                statuses.put(ComponentStatus.HorizontalMove, true);
            } else if (keyboardInput.getKeyA()) {
                animationHandler.getAnimation().setDirection(false);
                if(statuses.get(ComponentStatus.IsOnLadder)){
                    collideBox.moveByX(-5);
                }else {
                    collideBox.moveByX(-5);
                }
                statuses.put(ComponentStatus.HorizontalMove, true);
            }
        }

        // max jump will be implemented with a timer
        // this was a method appropriate to my system
        boolean jumpingTimer = timersHandler.getTimer(getType().name()).getTimerState();

        // jumping logic
        if (!statuses.get(ComponentStatus.IsOnLadder) &&
                ((statuses.get(ComponentStatus.BottomCollision) && !jumpingTimer && keyboardInput.getSpace() && jumpsCounter == 0) ||
                (jumpsCounter == 1 && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace()))) {
            timersHandler.getTimer(getType().name()).resetTimer();
            jumpsCounter++;
        }

        // movement on jumping logic
        if (jumpingTimer && !statuses.get(ComponentStatus.TopCollision)) {
            if (jumpsCounter == 1) {
                collideBox.moveByY(-7);
            } else {
                collideBox.moveByY(-9);
            }
        } else if (!statuses.get(ComponentStatus.IsOnLadder)&&
                (!statuses.get(ComponentStatus.BottomCollision) || statuses.get(ComponentStatus.TopCollision))) {
            collideBox.moveByY(9);
        }

        // if the top collision has occurred then stop timer earlier
        if (statuses.get(ComponentStatus.IsOnLadder) || (statuses.get(ComponentStatus.TopCollision) && jumpsCounter == 2)) {
            timersHandler.getTimer(getType().name()).finishEarlier();
        }

        // attack event logic
        if (!statuses.get(ComponentStatus.IsOnLadder) && keyboardInput.getKeyE() && !keyboardInput.isPreviousKeyE()) {
            statuses.put(ComponentStatus.Attack, true);
        }

        // climb on ladder logic
        if(statuses.get(ComponentStatus.IsOnLadder)) {
            if (keyboardInput.getPreviousKeyW()) {
                collideBox.moveByY(-2);
                statuses.put(ComponentStatus.IsMovingOnLadder , true);
            } else if(keyboardInput.getKeyS()){
                collideBox.moveByY(2);
                statuses.put(ComponentStatus.IsMovingOnLadder , true);
            }else {
                statuses.put(ComponentStatus.IsMovingOnLadder , false);
            }
        }

        // combo attack animation logic
        if (statuses.get(ComponentStatus.Attack) && animationHandler.getAnimation().animationIsOver()) {
            statuses.put(ComponentStatus.Attack, false);
            statuses.put(ComponentStatus.FirstHit, false);

            attackComboIndex++;
            if (attackComboIndex > 2) {
                attackComboIndex = 0;
            }
        }

        // animation logic
        if (statuses.get(ComponentStatus.Death)) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(AnimationType.BikerDeath, collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.IsOnLadder)) {
                animationHandler.changeAnimation(AnimationType.BikerClimb, collideBox.getPosition());
                if(statuses.get(ComponentStatus.IsMovingOnLadder)){
                    animationHandler.getAnimation().unlockAtLastFrame();
                }else {
                    animationHandler.getAnimation().lockAtLastFrame();
                }
            } else if (jumpsCounter == 0) {
                if (statuses.get(ComponentStatus.Attack)) {
                    animationHandler.changeAnimation(attackCombo[attackComboIndex], collideBox.getPosition());
                } else if (statuses.get(ComponentStatus.Hurt)) {
                    if (animationHandler.getAnimation().repeatsAreOver()) {
                        statuses.put(ComponentStatus.Hurt, false);
                    }
                } else {
                    if (statuses.get(ComponentStatus.HorizontalMove)) {
                        animationHandler.changeAnimation(AnimationType.BikerRun, collideBox.getPosition());
                    } else {
                        animationHandler.changeAnimation(AnimationType.BikerIdle, collideBox.getPosition());
                    }
                }
            } else if (jumpsCounter == 1) { // simple jump
                animationHandler.changeAnimation(AnimationType.BikerJump, collideBox.getPosition());
            } else if (jumpsCounter == 2) { // double jump
                animationHandler.changeAnimation(AnimationType.BikerDoubleJump, collideBox.getPosition());
            }
        }

        camera.setTargetPosition(collideBox.getMinX());
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Player));
    }
    @Override
    public void draw() {
        animationHandler.draw();
    }
    @Override
    public ComponentType getType() {
        return ComponentType.Player;
    }
}
