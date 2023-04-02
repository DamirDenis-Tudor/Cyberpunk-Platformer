package Components.DinamicComponents.Characters;

import Components.DinamicComponents.DinamicComponent;
import Enums.AnimationNames;
import Enums.ComponentNames;
import Enums.MessageNames;
import Input.KeyboardInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;

import static Utils.Constants.velocity;

public class Player extends DinamicComponent {
    private final KeyboardInput keyboardInput;
    private final TimersHandler timersHandler;
    private final Camera camera;
    private final AnimationHandler animationHandler;
    private boolean bottomCollision = false;
    private boolean topCollision = false;
    private boolean attack = false;
    private boolean hurt = false;
    private boolean firstHit = false;
    private boolean death = false;
    private Integer jumpsCounter = 0;
    private final AnimationNames[] attackCombo;
    private int attackComboIndex = 0;

    private int health = 100;

    public Player(Scene scene, Coordinate<Integer> position) throws Exception {
        this.scene = scene;

        keyboardInput = KeyboardInput.getInstance();
        timersHandler = TimersHandler.getInstance();
        camera = Camera.getInstance();
        camera.setPastTargetPosition(position.getPosX());

        animationHandler = new AnimationHandler();

        collideBox = new Rectangle(position, 0, 0);

        animationHandler.setPosition(collideBox.getPosition());
        animationHandler.changeAnimation(AnimationNames.BikerIdle, collideBox.getPosition());
        collideBox = animationHandler.getAnimation().getRectangle();

        attackCombo = new AnimationNames[3];
        attackCombo[0] = AnimationNames.BikerAttack1;
        attackCombo[1] = AnimationNames.BikerAttack2;
        attackCombo[2] = AnimationNames.BikerAttack3;

        timersHandler.addTimer(new Timer(0.30f), getType().name());
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.getSource()) {
            case Map -> {
                if (message.getType() == MessageNames.ActivateBottomCollision) {
                    bottomCollision = true;
                    topCollision = false;
                    jumpsCounter = 0;
                } else if (message.getType() == MessageNames.DeactivateBottomCollision) {
                    bottomCollision = false;
                } else if (message.getType() == MessageNames.ActivateTopCollision) {
                    topCollision = true;
                }
            }
            case BasicEnemy -> {
                if (message.getType() == MessageNames.Attack && !attack) {
                    animationHandler.changeAnimation(AnimationNames.BikerHurt, collideBox.getPosition());
                    animationHandler.getAnimation().setRepeats(2);
                    hurt = true;
                    health -= 1;
                    if (health <= 0) {
                        death = true;
                        setActiveStatus(false);
                        scene.notify(new Message(MessageNames.PlayerDeath , ComponentNames.Player));
                    }
                }
            }

        }
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) throws Exception {
        if (component.getActiveStatus()) {
            if (component.getType() == ComponentNames.Map) {

            } else {
                if (collideBox.intersects(component.getCollideBox()) && attack) {
                    if (!firstHit) {
                        component.notify(new Message(MessageNames.Attack, ComponentNames.Player));
                        firstHit = true;
                    }
                }
            }
        }
    }

    @Override
    public void update() throws Exception {
        boolean horizontalMove = false;
        // horizontal movement
        if ((!attack) || jumpsCounter != 0) {
            if (keyboardInput.getKeyD()) {
                animationHandler.getAnimation().setDirection(true);
                collideBox.moveByX(velocity);
                horizontalMove = true;
            } else if (keyboardInput.getKeyA()) {
                animationHandler.getAnimation().setDirection(false);
                collideBox.moveByX(-velocity);
                horizontalMove = true;
            }
        }

        // max jump will be implemented with a timer
        // this was a method appropriate to my system
        boolean jumpingTimer = timersHandler.getTimer(getType().name()).getTimerState();

        // -> If the player is on the ground, the space key has been pressed,
        //    and the timer is off, then we can start the "jump".
        // -> Or if the timer is off, double jump hasn't occurred and the space key
        //    has been released and then pressed again => jump
        if ((bottomCollision && !jumpingTimer && keyboardInput.getSpace() && jumpsCounter == 0) ||
                (jumpsCounter == 1 && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace())) {
            timersHandler.getTimer(getType().name()).resetTimer();
            jumpsCounter++;
        }

        // if the timer in on and top collision hasn't occurred then move up
        if (jumpingTimer && !topCollision) {
            if (jumpsCounter == 1) {
                collideBox.moveByY(-7);
            } else {
                collideBox.moveByY(-9);
            }
        } // indeed move down
        else if (!bottomCollision || topCollision) {
            collideBox.moveByY(9);
        }

        // if the top collision has occurred then stop timer earlier
        if (topCollision && jumpsCounter == 2) {
            timersHandler.getTimer(getType().name()).finishEarlier();
        }

        // make a request to handle the collisions
        scene.notify(new Message(MessageNames.HandleCollision, ComponentNames.Player));

        if (keyboardInput.getKeyE() && !keyboardInput.isPreviousKeyE()) {
            attack = true;
            System.out.println();
            System.out.println();
            System.out.println();
        }

        if (attack && animationHandler.getAnimation().animationIsOver()) {
            attack = false;
            firstHit = false;
            attackComboIndex++;
            if (attackComboIndex > 2) {
                attackComboIndex = 0;
            }

        }

        // animation logic
        if (death) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(AnimationNames.BikerDeath, collideBox.getPosition());
        } else {
            if (jumpsCounter == 0) {
                if (attack) {
                    animationHandler.changeAnimation(attackCombo[attackComboIndex], collideBox.getPosition());
                } else if (hurt) {
                    if (animationHandler.getAnimation().repeatsAreOver()) {
                        hurt = false;
                    }
                } else {
                    if (horizontalMove) {
                        animationHandler.changeAnimation(AnimationNames.BikerRun, collideBox.getPosition());
                    } else {
                        animationHandler.changeAnimation(AnimationNames.BikerIdle, collideBox.getPosition());
                    }
                }
            } else if (jumpsCounter == 1) {
                animationHandler.changeAnimation(AnimationNames.BikerJump, collideBox.getPosition());
            } else if (jumpsCounter == 2) {
                animationHandler.changeAnimation(AnimationNames.BikerDoubleJump, collideBox.getPosition());
            }
        }

        camera.setTargetPosition(collideBox.getMinX());
        animationHandler.update();
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentNames getType() {
        return ComponentNames.Player;
    }
}
