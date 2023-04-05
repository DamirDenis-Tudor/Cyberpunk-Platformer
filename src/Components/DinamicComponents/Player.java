package Components.DinamicComponents;

import Components.StaticComponents.AnimationHandler;
import Enums.*;
import Input.KeyboardInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Window.Camera;

import java.util.HashMap;
import java.util.Map;

public class Player extends DynamicComponent {
    private final TimersHandler timersHandler;
    private final AnimationHandler animationHandler;
    private final KeyboardInput keyboardInput;
    private final Camera camera;
    private final Map<ComponentStatus, Boolean> statuses;
    private final Map<GeneralAnimationTypes, AnimationType> animationsType;
    private int health = 100;
    private Integer jumpsCounter = 0;
    private final AnimationType[] attackCombo;
    private int attackComboIndex = 0;

    public Player(Scene scene, Coordinate<Integer> position , ComponentType type) throws Exception {
        this.scene = scene;

        keyboardInput = KeyboardInput.getInstance();

        timersHandler = TimersHandler.getInstance();
        timersHandler.addTimer(new Timer(0.30f), getType().name());

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.BikerIdle, position);
        collideBox = animationHandler.getAnimation().getRectangle();

        camera = Camera.getInstance();
        camera.setFocusComponentPosition(collideBox.getPosition());

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
        statuses.put(ComponentStatus.TryingToOpenOrPickSomething, false);

        animationsType = new HashMap<>();
        attackCombo = new AnimationType[3];
        switch (type) {
            case Biker -> {
                attackCombo[0] = AnimationType.BikerAttack1;
                attackCombo[1] = AnimationType.BikerAttack2;
                attackCombo[2] = AnimationType.BikerAttack3;
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.BikerIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.BikerRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.BikerAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.BikerHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.BikerClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.BikerJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.BikerDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.BikerDeath);
            }
            case Cyborg -> {
                attackCombo[0] = AnimationType.CyborgAttack1;
                attackCombo[1] = AnimationType.CyborgAttack2;
                attackCombo[2] = AnimationType.CyborgAttack3;
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.CyborgIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.CyborgRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.CyborgAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.CyborgHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.CyborgClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.CyborgJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.CyborgDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.CyborgDeath);
            }
            case Punk -> {
                attackCombo[0] = AnimationType.PunkAttack1;
                attackCombo[1] = AnimationType.PunkAttack2;
                attackCombo[2] = AnimationType.PunkAttack3;
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.PunkIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.PunkRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.PunkAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.PunkHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.PunkClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.PunkJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.PunkDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.PunkDeath);
            }
        }
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
            case Enemy-> {
                if (message.getType() == MessageType.Attack && !statuses.get(ComponentStatus.Attack)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Hurt), collideBox.getPosition());
                    animationHandler.getAnimation().setRepeats(2);
                    statuses.put(ComponentStatus.Hurt, true);
                    health -= 1;
                    if (health <= 0) {
                        statuses.put(ComponentStatus.Death, true);
                        setActiveStatus(false);
                        scene.notify(new Message(MessageType.PlayerDeath, ComponentType.Player,getId()));
                    }
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {
        switch (component.getType()) {
            case Enemy -> {
                if (collideBox.intersects(component.getCollideBox()) &&
                        statuses.get(ComponentStatus.Attack) &&
                        !statuses.get(ComponentStatus.FirstHit)) {
                    component.notify(new Message(MessageType.Attack, ComponentType.Player,getId()));
                    statuses.put(ComponentStatus.FirstHit, true);
                }
            }
            case Chest -> {
                if(collideBox.intersects(component.getCollideBox()) && statuses.get(ComponentStatus.TryingToOpenOrPickSomething)) {
                    component.notify(new Message(MessageType.ReadyToBeOpened , ComponentType.Player,getId()));
                }
            }
            case Gun -> {
                if(collideBox.intersects(component.getCollideBox()) && statuses.get(ComponentStatus.TryingToOpenOrPickSomething)) {
                    component.notify(new Message(MessageType.IsPickedUp , ComponentType.Player,getId()));
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

        // open chest logic
        if(keyboardInput.getKeyQ()){
            statuses.put(ComponentStatus.TryingToOpenOrPickSomething, true);
        }else {
            statuses.put(ComponentStatus.TryingToOpenOrPickSomething, false);
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
            animationsType.put(GeneralAnimationTypes.Attack , attackCombo[attackComboIndex]);
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
            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Death), collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.IsOnLadder)) {
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Climb), collideBox.getPosition());
                if (statuses.get(ComponentStatus.IsMovingOnLadder)) {
                    animationHandler.getAnimation().unlock();
                } else {
                    animationHandler.getAnimation().lockAtLastFrame();
                }
            } else if (jumpsCounter == 0) {
                if (statuses.get(ComponentStatus.Attack)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Attack), collideBox.getPosition());
                } else if (statuses.get(ComponentStatus.Hurt)) {
                    if (animationHandler.getAnimation().repeatsAreOver()) {
                        statuses.put(ComponentStatus.Hurt, false);
                    }
                } else {
                    if (statuses.get(ComponentStatus.HorizontalMove)) {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Run), collideBox.getPosition());
                    } else {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Idle), collideBox.getPosition());
                    }
                }
            } else if (jumpsCounter == 1) { // simple jump
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Jump), collideBox.getPosition());
            } else if (jumpsCounter == 2) { // double jump
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.DoubleJump), collideBox.getPosition());
            }
        }
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Player,getId()));
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
