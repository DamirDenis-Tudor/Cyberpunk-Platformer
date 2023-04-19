package Enums;

/**
 * This class facilitates component interaction by providing flexibility in adding new behavioral interactions.
 */
public enum MessageType {
    // Components messages
    HandleCollision, HasCollision, Destroy, ActivateTopCollision, ActivateBottomCollision, DeactivateBottomCollision, IsOnLadder, IsNoLongerOnLadder, LeftCollision, RightCollision, LeftCollisionWithOther, RightCollisionWithOther, Attack, PlayerDeath, ReadyToBeOpened, SpawnGun, IsPickedUp, LaunchBullet, PLayerDirectionRight, PlayerDirectionLeft, BulletLaunchRight, BulletLaunchLeft, HideGun, ShowGun, Shoot, OnPlatform, OnHelicopter, DetachedFromHelicopter, SceneHasBeenActivated, ButtonClicked,

    // Scenes Messages
    NewGame,SaveGame,LoadGame,

    SaveHasBeenAdded
}
