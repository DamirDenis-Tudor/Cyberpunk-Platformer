package Enums;

/**
 * This class contains all possible component statuses, providing flexibility for implementing game logic.
 * Each component will have a map of statuses represented by boolean values.
 */
public enum ComponentStatus {
    BOTTOM_COLLISION,
    TOP_COLLISION,
    RIGHT_COLLISION,
    LEFT_COLLISION,
    IS_ON_LADDER,
    IS_MOVING_ON_LADDER,
    RIGHT_COLLISION_WITH_OTHER,
    LEFT_COLLISION_WITH_OTHER,
    FALLING,
    HORIZONTAL_MOVE,
    ATTACK,
    HURT,
    FIRST_HIT,
    DEATH,
    HAS_ENEMY_COLLISION,
    IDLE,
    TRYING_TO_OPEN_OR_PICK_SOMETHING,
    GUN_PICKED,
    IS_OPENED,
    HAS_DROPPED_WEAPON,
    IS_PICKED_UP,
    HAS_LAUNCHED_BULLET,
    HIDE,
    ON_HELICOPTER,
    HAS_PLAYER,
    DETACHED_FROM_HELICOPTER,
    HAS_DETECTED_PLAYER,
    NEEDS_RECALIBRATION
}
