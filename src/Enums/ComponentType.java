package Enums;

import java.io.Serializable;

/**
 * This class contains all possible base-types or subtypes of components.
 */
public enum ComponentType implements Serializable {
    None,

    Player, Biker , Punk , Cyborg ,

    Map , GreenCity , IndustrialCity,
    Enemy, BaseballEnemy, SkaterEnemy, GunnerEnemy,MachineGunEnemy, Cat1,Dog1,Cat2,Dog2,
    Chest , Gun, Bullet,Platform, Helicopter,
    Gun1, Gun2, Gun3, Gun4, Gun5, Gun6, Gun7, Gun8, Gun9, Gun10,
    Bullet1, Bullet2, Bullet3, Bullet4, Bullet5, Bullet6, Bullet7, Bullet8, Bullet9, Bullet10,
    NewGameButton, LoadButton , SettingsButton ,ExitButton,Back,BackToMenu, Continue , DeleteSave , DeleteLatestSave, LoadSave,SaveButton,SaveInfo,

    SceneHandler,Scene


}
