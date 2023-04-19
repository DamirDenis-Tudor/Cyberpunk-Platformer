package Enums;

/**
 * This class contains all possible component statuses, providing flexibility for implementing game logic.
 * Each component will have a map of statuses represented by boolean values.
 */
public enum ComponentStatus {
    BottomCollision,
    TopCollision,
    RightCollision,
    LeftCollision,
    IsOnLadder,
    IsMovingOnLadder,
    RightCollisionWithOther,
    LeftCollisionWithOther,

    Falling,
    HorizontalMove,
    Attack,
    Hurt,
    FirstHit,
    Death,
    HasEnemyCollision,
    Idle,
    TryingToOpenOrPickSomething,
    GunPicked,
    IsOpened,
    HasDroppedWeapon,
    IsPickedUp,
    HasLaunchedBullet,
    Hide,
    OnHelicopter,
    HasPlayer,
    DetachedFromHelicopter,
    HasDetectedPLayer,
    NeedsRecalibration
}
