package Components.GameComponents;

import Enums.AnimationType;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.GeneralAnimationTypes;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Enums.ComponentType.DRONE;

/**
 * This class loads predefined characteristics of a component.
 */
public class CharacterisesGenerator {
    public static List<AnimationType> generateAttackComboFor(ComponentType type) {
        List<AnimationType> attackCombo = new ArrayList<>();
        switch (type) {
            case BIKER -> {
                attackCombo.add(AnimationType.BikerAttack1);
                attackCombo.add(AnimationType.BikerPunch);
                attackCombo.add(AnimationType.BikerAttack2);
                attackCombo.add(AnimationType.BikerAttack3);
            }
            case CYBORG -> {
                attackCombo.add(AnimationType.CyborgAttack1);
                attackCombo.add(AnimationType.CyborgAttack2);
                attackCombo.add(AnimationType.CyborgAttack3);
            }
            case PUNK -> {
                attackCombo.add(AnimationType.PunkAttack1);
                attackCombo.add(AnimationType.PunkPunch);
                attackCombo.add(AnimationType.PunkAttack2);
                attackCombo.add(AnimationType.PunkAttack3);
            }
        }
        return attackCombo;
    }

    public static Map<GeneralAnimationTypes, AnimationType> generateAnimationTypesFor(ComponentType type ,int id) {
        Map<GeneralAnimationTypes, AnimationType> animationsType = new HashMap<>();
        switch (type) {
            case BIKER -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.BikerIdle);
                animationsType.put(GeneralAnimationTypes.RUN, AnimationType.BikerRun);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.BikerAttack1);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.BikerHurt);
                animationsType.put(GeneralAnimationTypes.CLIMB, AnimationType.BikerClimb);
                animationsType.put(GeneralAnimationTypes.JUMP, AnimationType.BikerJump);
                animationsType.put(GeneralAnimationTypes.DOUBLE_JUMP, AnimationType.BikerDoubleJump);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.BikerDeath);
                animationsType.put(GeneralAnimationTypes.RUN_GUN, AnimationType.BikerRunGun);
                animationsType.put(GeneralAnimationTypes.IDLE_GUN, AnimationType.BikerIdleGun);
                animationsType.put(GeneralAnimationTypes.JUMP_GUN, AnimationType.BikerJumpGun);
                animationsType.put(GeneralAnimationTypes.FALLING, AnimationType.BikerFall);
            }
            case CYBORG -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.CyborgIdle);
                animationsType.put(GeneralAnimationTypes.RUN, AnimationType.CyborgRun);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.CyborgAttack1);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.CyborgHurt);
                animationsType.put(GeneralAnimationTypes.CLIMB, AnimationType.CyborgClimb);
                animationsType.put(GeneralAnimationTypes.JUMP, AnimationType.CyborgJump);
                animationsType.put(GeneralAnimationTypes.DOUBLE_JUMP, AnimationType.CyborgDoubleJump);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.CyborgDeath);
                animationsType.put(GeneralAnimationTypes.RUN_GUN, AnimationType.CyborgRunGun);
                animationsType.put(GeneralAnimationTypes.IDLE_GUN, AnimationType.CyborgIdleGun);
                animationsType.put(GeneralAnimationTypes.JUMP_GUN, AnimationType.CyborgJumpGun);
            }
            case PUNK -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.PunkIdle);
                animationsType.put(GeneralAnimationTypes.RUN, AnimationType.PunkRun);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.PunkAttack1);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.PunkHurt);
                animationsType.put(GeneralAnimationTypes.CLIMB, AnimationType.PunkClimb);
                animationsType.put(GeneralAnimationTypes.JUMP, AnimationType.PunkJump);
                animationsType.put(GeneralAnimationTypes.DOUBLE_JUMP, AnimationType.PunkDoubleJump);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.PunkDeath);
                animationsType.put(GeneralAnimationTypes.RUN_GUN, AnimationType.PunkRunGun);
                animationsType.put(GeneralAnimationTypes.IDLE_GUN, AnimationType.PunkIdleGun);
                animationsType.put(GeneralAnimationTypes.JUMP_GUN, AnimationType.PunkJumpGun);
            }
            case DOG_1 -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Dog1Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Dog1Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Dog1Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Dog1Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Dog1Death);
            }
            case DOG_2 -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Dog2Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Dog2Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Dog2Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Dog2Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Dog2Death);
            }
            case CAT_1 -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Cat1Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Cat1Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Cat1Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Cat1Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Cat1Death);
            }
            case CAT_2 -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Cat2Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Cat2Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Cat2Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Cat2Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Cat2Death);
            }

            case BASEBALL_ENEMY -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Enemy1Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Enemy1Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Enemy1Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Enemy1Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Enemy1Death);
            }
            case SKATER_ENEMY -> {
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Enemy3Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Enemy3Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Enemy3Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Enemy3Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Enemy3Death);
            }
            case GUNNER_ENEMY -> {
                TimersHandler.get().addTimer(new Timer(0.5f) , type.name()+id);
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Enemy2Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Enemy2Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Enemy2Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Enemy2Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Enemy2Death);
            }
            case MACHINE_GUN_ENEMY -> {
                TimersHandler.get().addTimer(new Timer(0.4f) , type.name()+id);
                animationsType.put(GeneralAnimationTypes.IDLE, AnimationType.Enemy6Idle);
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Enemy6Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Enemy6Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Enemy6Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Enemy6Death);
            }
            case AIRPLANE -> {
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Airplane);
            }
            case DRONE_ENEMY -> {
                animationsType.put(GeneralAnimationTypes.WALK, AnimationType.Enemy5Walk);
                animationsType.put(GeneralAnimationTypes.ATTACK, AnimationType.Enemy5Attack);
                animationsType.put(GeneralAnimationTypes.HURT, AnimationType.Enemy5Hurt);
                animationsType.put(GeneralAnimationTypes.DEATH, AnimationType.Enemy5Death);
            }
        }
        return animationsType;
    }

    public static Map<ComponentStatus, Boolean> generateStatusesFor(ComponentType type) {
        Map<ComponentStatus, Boolean> statuses = new HashMap<>();
        switch (type) {
            case PLAYER -> {
                statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
                statuses.put(ComponentStatus.TOP_COLLISION, false);
                statuses.put(ComponentStatus.IS_ON_LADDER, false);
                statuses.put(ComponentStatus.HORIZONTAL_MOVE, false);
                statuses.put(ComponentStatus.HURT, false);
                statuses.put(ComponentStatus.DEATH, false);
                statuses.put(ComponentStatus.FIRST_HIT, false);
                statuses.put(ComponentStatus.ATTACK, false);
                statuses.put(ComponentStatus.IS_MOVING_ON_LADDER, false);
                statuses.put(ComponentStatus.TRYING_TO_OPEN_OR_PICK_SOMETHING, false);
                statuses.put(ComponentStatus.GUN_PICKED, false);
                statuses.put(ComponentStatus.ON_HELICOPTER, false);
                statuses.put(ComponentStatus.DETACHED_FROM_HELICOPTER, false);
            }
            case GROUND_ENEMY -> {
                statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
                statuses.put(ComponentStatus.LEFT_COLLISION, false);
                statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                statuses.put(ComponentStatus.LEFT_COLLISION_WITH_OTHER, false);
                statuses.put(ComponentStatus.RIGHT_COLLISION_WITH_OTHER, false);
                statuses.put(ComponentStatus.HORIZONTAL_MOVE, false);
                statuses.put(ComponentStatus.HURT, false);
                statuses.put(ComponentStatus.DEATH, false);
                statuses.put(ComponentStatus.FIRST_HIT, false);
                statuses.put(ComponentStatus.ATTACK, false);
                statuses.put(ComponentStatus.HAS_ENEMY_COLLISION, false);
                statuses.put(ComponentStatus.HAS_DETECTED_PLAYER, false);
                statuses.put(ComponentStatus.IDLE, false);
            }
            case AIRPLANE -> {
                statuses.put(ComponentStatus.ATTACK, false);
                statuses.put(ComponentStatus.LEFT_COLLISION, false);
                statuses.put(ComponentStatus.RIGHT_COLLISION, false);
            }
            case DRONE_ENEMY -> {
                statuses.put(ComponentStatus.ATTACK, false);
                statuses.put(ComponentStatus.BOTTOM_COLLISION, true);
                statuses.put(ComponentStatus.TOP_COLLISION, false);
                statuses.put(ComponentStatus.HURT , false);
                statuses.put(ComponentStatus.DEATH , false);
                statuses.put(ComponentStatus.HAS_DETECTED_PLAYER , false);
            }
            case GUN -> {
                statuses.put(ComponentStatus.IS_PICKED_UP, false);
                statuses.put(ComponentStatus.HAS_LAUNCHED_BULLET, false);
                statuses.put(ComponentStatus.HIDE, false);
                statuses.put(ComponentStatus.NEEDS_RECALIBRATION, false);
                statuses.put(ComponentStatus.GUN_ENABLED , false);
            }
        }
        return statuses;
    }

    public static int getVelocityFor(ComponentType type) {
        int velocity = 0;
        switch (type) {
            case PLAYER -> velocity = Constants.PLAYER_VELOCITY;
            case SKATER_ENEMY -> velocity = Constants.SKATER_ENEMY_VELOCITY;
            case BASEBALL_ENEMY -> velocity = Constants.BASEBALL_ENEMY_VELOCITY;
            case GUNNER_ENEMY, MACHINE_GUN_ENEMY -> velocity = Constants.GUNNAR_ENEMY_VELOCITY;
            case CAT_1, CAT_2, DOG_1, DOG_2 -> velocity = Constants.ANIMAL_ENEMY_VELOCITY;
            case HELICOPTER -> velocity = Constants.HELICOPTER_VELOCITY;
            case PLATFORM -> velocity = Constants.PLATFORM_VELOCITY;
            case AIRPLANE -> velocity = Constants.AIRPLANE_VELOCITY;
            case DRONE_ENEMY -> velocity = Constants.DRONE_VELOCITY;
        }
        return velocity;
    }

    public static ComponentType getGunTypeForEnemy(ComponentType type){
        ComponentType type1 = ComponentType.NONE;
        switch (type){
            case GUNNER_ENEMY -> type1 = ComponentType.GUN_1;
            case MACHINE_GUN_ENEMY -> type1 = ComponentType.GUN_2;
        }
        return type1;
    }
}
