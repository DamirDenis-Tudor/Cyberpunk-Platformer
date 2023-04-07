package Enums;

/**
 * This class facilitates component interaction by providing flexibility in adding new behavioral interactions.
 */
public enum MessageType {
    HandleCollision,
    HasCollision,
    Destroy,
    ActivateTopCollision,
    ActivateBottomCollision,
    DeactivateBottomCollision,
    FallingPossibility,
    IsOnLadder,
    IsNoLongerOnLadder,
    LeftCollision,
    RightCollision,
    LeftCollisionWithOther,
    RightCollisionWithOther,
    EnemyDeath,
    Attack,
    PlayerDeath,
    ReadyToBeOpened,
    SpawnGun,
    IsPickedUp,
    LaunchBullet,
    PLayerDirectionRight,
    PlayerDirectionLeft,
    BulletLaunchRight,
    BulletLauchLeft,
    HideGun,
    ShowGun,
    Shoot,
    OnPlatform,
    OnHelicopter
}
