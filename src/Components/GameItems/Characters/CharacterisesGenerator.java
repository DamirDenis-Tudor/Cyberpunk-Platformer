package Components.GameItems.Characters;

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

public class CharacterisesGenerator {
    public static List<AnimationType> generateAttackComboFor(ComponentType type) {
        List<AnimationType> attackCombo = new ArrayList<>();
        switch (type) {
            case Biker -> {
                attackCombo.add(AnimationType.BikerAttack1);
                attackCombo.add(AnimationType.BikerPunch);
                attackCombo.add(AnimationType.BikerAttack2);
                attackCombo.add(AnimationType.BikerAttack3);
            }
            case Cyborg -> {
                attackCombo.add(AnimationType.CyborgAttack1);
                attackCombo.add(AnimationType.CyborgAttack2);
                attackCombo.add(AnimationType.CyborgAttack3);
            }
            case Punk -> {
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
            case Biker -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.BikerIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.BikerRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.BikerAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.BikerHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.BikerClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.BikerJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.BikerDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.BikerDeath);
                animationsType.put(GeneralAnimationTypes.RunGun, AnimationType.BikerRunGun);
                animationsType.put(GeneralAnimationTypes.IdleGun, AnimationType.BikerIdleGun);
                animationsType.put(GeneralAnimationTypes.JumpGun, AnimationType.BikerJumpGun);
                animationsType.put(GeneralAnimationTypes.Falling, AnimationType.BikerFall);
            }
            case Cyborg -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.CyborgIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.CyborgRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.CyborgAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.CyborgHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.CyborgClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.CyborgJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.CyborgDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.CyborgDeath);
                animationsType.put(GeneralAnimationTypes.RunGun, AnimationType.CyborgRunGun);
                animationsType.put(GeneralAnimationTypes.IdleGun, AnimationType.CyborgIdleGun);
                animationsType.put(GeneralAnimationTypes.JumpGun, AnimationType.CyborgJumpGun);
            }
            case Punk -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.PunkIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.PunkRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.PunkAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.PunkHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.PunkClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.PunkJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.PunkDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.PunkDeath);
                animationsType.put(GeneralAnimationTypes.RunGun, AnimationType.PunkRunGun);
                animationsType.put(GeneralAnimationTypes.IdleGun, AnimationType.PunkIdleGun);
                animationsType.put(GeneralAnimationTypes.JumpGun, AnimationType.PunkJumpGun);
            }
            case Dog1 -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Dog1Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Dog1Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Dog1Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Dog1Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Dog1Death);
            }
            case Dog2 -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Dog2Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Dog2Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Dog2Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Dog2Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Dog2Death);
            }
            case Cat1 -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Cat1Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Cat1Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Cat1Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Cat1Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Cat1Death);
            }
            case Cat2 -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Cat2Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Cat2Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Cat2Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Cat2Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Cat2Death);
            }

            case BaseballEnemy -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Enemy1Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Enemy1Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Enemy1Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Enemy1Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Enemy1Death);
            }
            case SkaterEnemy -> {
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Enemy3Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Enemy3Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Enemy3Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Enemy3Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Enemy3Death);
            }
            case GunnerEnemy -> {
                TimersHandler.get().addTimer(new Timer(0.5f) , type.name()+id);
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Enemy2Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Enemy2Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Enemy2Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Enemy2Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Enemy2Death);
            }
            case MachineGunEnemy -> {
                TimersHandler.get().addTimer(new Timer(0.2f) , type.name()+id);
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.Enemy6Idle);
                animationsType.put(GeneralAnimationTypes.Walk, AnimationType.Enemy6Walk);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.Enemy6Attack);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.Enemy6Hurt);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.Enemy6Death);
            }
        }
        return animationsType;
    }

    public static Map<ComponentStatus, Boolean> generateStatusesFor(ComponentType type) {
        Map<ComponentStatus, Boolean> statuses = new HashMap<>();
        switch (type) {
            case Player -> {
                statuses.put(ComponentStatus.BottomCollision, false);
                statuses.put(ComponentStatus.TopCollision, false);
                statuses.put(ComponentStatus.IsOnLadder, false);
                statuses.put(ComponentStatus.HorizontalMove, false);
                statuses.put(ComponentStatus.Hurt, false);
                statuses.put(ComponentStatus.Death, false);
                statuses.put(ComponentStatus.FirstHit, false);
                statuses.put(ComponentStatus.Attack, false);
                statuses.put(ComponentStatus.IsMovingOnLadder, false);
                statuses.put(ComponentStatus.TryingToOpenOrPickSomething, false);
                statuses.put(ComponentStatus.GunPicked, false);
                statuses.put(ComponentStatus.OnHelicopter, false);
                statuses.put(ComponentStatus.DetachedFromHelicopter, false);
            }
            case Enemy -> {
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
                statuses.put(ComponentStatus.HasDetectedPLayer, false);
                statuses.put(ComponentStatus.Idle, false);
            }
        }
        return statuses;
    }

    public static int getVelocityFor(ComponentType type) {
        int velocity = 0;
        switch (type) {
            case Player -> velocity = Constants.playerVelocity;
            case SkaterEnemy -> velocity = Constants.skaterEnemyVelocity;
            case BaseballEnemy -> velocity = Constants.baseballEnemyVelocity;
            case GunnerEnemy, MachineGunEnemy -> velocity = Constants.gunnarEnemyVelocity;
            case Cat1, Cat2, Dog1, Dog2 -> velocity = Constants.animalEnemyVelocity;
            case Helicopter -> velocity = Constants.helicopterVelocity;
            case Platform -> velocity = Constants.platformVelocity;
        }
        return velocity;
    }

    public static ComponentType getGunTypeForEnemy(ComponentType type){
        ComponentType type1 = ComponentType.None;
        switch (type){
            case GunnerEnemy -> type1 = ComponentType.Gun1;
            case MachineGunEnemy -> type1 = ComponentType.Gun2;
        }
        return type1;
    }
}
