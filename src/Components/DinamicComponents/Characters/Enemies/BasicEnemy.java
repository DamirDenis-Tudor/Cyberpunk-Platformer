package Components.DinamicComponents.Characters.Enemies;

import Components.DinamicComponents.Characters.AnimationHandler;
import Components.DinamicComponents.DinamicComponent;
import Enums.AnimationNames;
import Enums.ComponentNames;
import Enums.MessageNames;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;

public class BasicEnemy extends DinamicComponent {
    private final AnimationHandler animationHandler;
    private final TimersHandler timersHandler;
    private boolean leftCollision = false;
    private boolean rightCollsion = false;
    private boolean bottomCollision = false;
    private boolean hurt = false;
    private int health = 100;
    private boolean death = false;
    private boolean firstHit = false;
    private boolean attack = false;
    private boolean hasDetectedPlayer = false;

    private boolean hasEnemyCollision = false;
    private boolean idle = false;

    public BasicEnemy(Scene scene, Coordinate<Integer> position) throws Exception {

        this.scene = scene;
        animationHandler = new AnimationHandler();
        timersHandler = TimersHandler.getInstance();

        timersHandler.addTimer(new Timer(1f), "stan" + getId());

        timersHandler.addTimer(new Timer(0.2f), "lock_target" + getId());

        animationHandler.changeAnimation(AnimationNames.Enemy1Idle, position);

        // takes a "reference" of the animation rectangle
        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.getSource()) {
            case Map -> {
                if (message.getType() == MessageNames.LeftCollision) {
                    leftCollision = true;
                    rightCollsion = false;
                } else if (message.getType() == MessageNames.RightCollision) {
                    rightCollsion = true;
                    leftCollision = false;
                } else if (message.getType() == MessageNames.ActivateBottomCollision) {
                    bottomCollision = true;
                }
            }
            case BasicEnemy -> {
                hasEnemyCollision = false;
                if (message.getType() == MessageNames.LeftCollisionWithOther) {
                    leftCollision = true;
                    rightCollsion = false;
                    hasEnemyCollision = true;
                } else if (message.getType() == MessageNames.RightCollisionWithOther) {
                    rightCollsion = true;
                    leftCollision = false;
                    hasEnemyCollision = true;
                } else if (message.getType() == MessageNames.EnemyDeath) {
                    hasEnemyCollision = false;
                }
            }
            case Player -> {
                if (message.getType() == MessageNames.Attack) {
                    animationHandler.changeAnimation(AnimationNames.Enemy1Hurt, collideBox.getPosition());
                    animationHandler.getAnimation().setRepeats(4);
                    hurt = true;
                    health -= 20;
                    if (health <= 0) {
                        scene.notify(new Message(MessageNames.EnemyDeath , ComponentNames.BasicEnemy));
                        death = true;
                        setActiveStatus(false);
                    }
                } else if (message.getType() == MessageNames.PlayerDeath) {
                    hasDetectedPlayer = false;
                    idle = false;
                    attack = false;
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) throws Exception {
        //   System.out.println("Enemy:"+getId() +" handle interation");
        if (component.getActiveStatus()) {
            switch (component.getType()) {
                case Player -> {
                    hasDetectedPlayer = true;
                    if (collideBox.intersects(component.getCollideBox()) && !hurt) {
                        attack = true;
                        if (!timersHandler.getTimer("lock_target" + getId()).getTimerState() && !firstHit) {
                            firstHit = true;
                            component.notify(new Message(MessageNames.Attack, ComponentNames.BasicEnemy));
                        }
                    } else {
                        if (attack && animationHandler.getAnimation().animationIsOver()) {
                            attack = false;
                        }
                    }
                    if (!hurt && !hasEnemyCollision && collideBox.getMinY() < component.getCollideBox().getCenterY() && collideBox.getMaxY() > component.getCollideBox().getCenterY()) {
                        if (collideBox.getCenterX() - component.getCollideBox().getCenterX() > 0) {
                            if (!animationHandler.getAnimation().getDirection() && leftCollision) {
                                idle = true;
                            } else {
                                rightCollsion = true;
                                leftCollision = false;
                            }
                        } else if (collideBox.getCenterX() - component.getCollideBox().getCenterX() < 0) {
                            if (animationHandler.getAnimation().getDirection() && rightCollsion) {
                                idle = true;
                            } else {
                                leftCollision = true;
                                rightCollsion = false;
                            }
                        }
                    } else {
                        idle = false;
                        hasDetectedPlayer = false;
                    }
                }
                case BasicEnemy -> {
                    if (component.getCollideBox().intersects(collideBox) && !hasDetectedPlayer) {
                        if (component.getCollideBox().getDx() > 0) {
                            component.notify(new Message(MessageNames.LeftCollisionWithOther, ComponentNames.BasicEnemy));
                        } else if (component.getCollideBox().getDx() < 0) {
                            component.notify(new Message(MessageNames.RightCollisionWithOther, ComponentNames.BasicEnemy));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() throws Exception {
        if (!bottomCollision) {
            collideBox.moveByY(8);
        }
        boolean horizontalMove = false;
        if (!attack && !hurt && !idle) {
            if (!leftCollision) {
                horizontalMove = true;
                animationHandler.getAnimation().setDirection(false);
                collideBox.moveByX(-1);
            } else if (!rightCollsion) {
                horizontalMove = true;
                animationHandler.getAnimation().setDirection(true);
                collideBox.moveByX(1);
            }
        }

        if (death) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(AnimationNames.Enemy1Death, collideBox.getPosition());
        } else {
            if (hurt) {
                if (animationHandler.getAnimation().repeatsAreOver()) {
                    hurt = false;
                }
            } else if (attack) {
                animationHandler.changeAnimation(AnimationNames.Enemy1Attack, collideBox.getPosition());
                if (animationHandler.getAnimation().animationIsOver()) {
                    firstHit = false;
                }
            } else if (idle) {
                animationHandler.changeAnimation(AnimationNames.Enemy1Idle, collideBox.getPosition());
            } else if (horizontalMove) {
                animationHandler.changeAnimation(AnimationNames.Enemy1Walk, collideBox.getPosition());
            }
        }
        animationHandler.update();
        scene.notify(new Message(MessageNames.HandleCollision, ComponentNames.BasicEnemy));
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentNames getType() {
        return ComponentNames.BasicEnemy;
    }


}
