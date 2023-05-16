package Enums;

/**
 *  This class has all the animation names loaded and is used because, initially, the animations are loaded from a file using a string as an identifier.
 *  It would be time-consuming to have to recall each animation name individually, so having them all loaded in the class is more efficient.
 */
public enum AnimationType {
    BikerPunch("BikerPunch"),
    BikerJump("BikerJump"),
    BikerIdle("BikerIdle"),
    BikerHurt("BikerHurt"),
    BikerDoubleJump("BikerDoubleJump"),
    BikerDeath("BikerDeath"),
    BikerClimb("BikerClimb"),
    BikerAttack3("BikerAttack3"),
    BikerAttack2("BikerAttack2"),
    BikerAttack1("BikerAttack1"),
    BikerRunAttack("BikerRunAttack"),
    BikerRun("BikerRun"),
    PunkRunAttack("PunkRunAttack"),
    PunkRun("PunkRun"),
    PunkPunch("PunkPunch"),
    PunkJump("PunkJump"),
    PunkIdle("PunkIdle"),
    PunkHurt("PunkHurt"),
    PunkDoubleJump("PunkDoubleJump"),
    PunkDeath("PunkDeath"),
    PunkClimb("PunkClimb"),
    PunkAttack3("PunkAttack3"),
    PunkAttack2("PunkAttack2"),
    PunkAttack1("PunkAttack1"),
    CyborgRunAttack("CyborgRunAttack"),
    CyborgRun("CyborgRun"),
    CyborgPunch("CyborgPunch"),
    CyborgJump("CyborgJump"),
    CyborgIdle("CyborgIdle"),
    CyborgHurt("CyborgHurt"),
    CyborgDoubleJump("CyborgDoubleJump"),
    CyborgDeath("CyborgDeath"),
    CyborgClimb("CyborgClimb"),
    CyborgAttack3("CyborgAttack3"),
    CyborgAttack2("CyborgAttack2"),
    CyborgAttack1("CyborgAttack1"),
    BikerWalkAttack("BikerWalkAttack"),
    BikerWalk("BikerWalk"),
    BikerUse("BikerUse"),
    BikerTalk("BikerTalk"),
    BikerSitdown("BikerSitdown"),
    BikerIdle2("BikerIdle2"),
    BikerHappy("BikerHappy"),
    BikerHang("BikerHang"),
    BikerFall("BikerFall"),
    BikerDash("BikerDash"),
    BikerAngry("BikerAngry"),
    PunkWalkAttack("PunkWalkAttack"),
    PunkWalk("PunkWalk"),
    PunkUse("PunkUse"),
    PunkTalk("PunkTalk"),
    PunkSitdown("PunkSitdown"),
    PunkIdle2("PunkIdle2"),
    PunkHang("PunkHang"),
    PunkFall("PunkFall"),
    PunkDash("PunkDash"),
    PunkAngry("PunkAngry"),
    PunkHappy("PunkHappy"),
    CyborgWalkAttack("CyborgWalkAttack"),
    CyborgWalk("CyborgWalk"),
    CyborgUse("CyborgUse"),
    CyborgTalk("CyborgTalk"),
    CyborgSitdown("CyborgSitdown"),
    CyborgIdle2("CyborgIdle2"),
    CyborgHappy("CyborgHappy"),
    CyborgHang("CyborgHang"),
    CyborgFall("CyborgFall"),
    CyborgDash("CyborgDash"),
    CyborgAngry("CyborgAngry"),
    Screen2("Screen2"),
    Screen1("Screen1"),
    Platform("Platform"),
    Platform1("Platform1"),
    Money("Money"),
    Hammer("Hammer"),
    Entry("Entry"),
    Chest1("Chest1"),
    Chest2("Chest2"),
    Card("Card"),
    Fountain("Fountain"),
    Dog1Walk("Dog1Walk"),
    Dog1Idle("Dog1Idle"),
    Dog1Hurt("Dog1Hurt"),
    Dog1Death("Dog1Death"),
    Dog1Attack("Dog1Attack"),
    Dog2Walk("Dog2Walk"),
    Dog2Idle("Dog2Idle"),
    Dog2Hurt("Dog2Hurt"),
    Dog2Death("Dog2Death"),
    Dog2Attack("Dog2Attack"),
    Cat1Walk("Cat1Walk"),
    Cat1Idle("Cat1Idle"),
    Cat1Hurt("Cat1Hurt"),
    Cat1Death("Cat1Death"),
    Cat1Attack("Cat1Attack"),
    Cat2Walk("Cat2Walk"),
    Cat2Idle("Cat2Idle"),
    Cat2Hurt("Cat2Hurt"),
    Cat2Death("Cat2Death"),
    Cat2Attack("Cat2Attack"),
    Rat1Walk("Rat1Walk"),
    Rat1Idle("Rat1Idle"),
    Rat1Hurt("Rat1Hurt"),
    Rat1Death("Rat1Death"),
    Rat2Walk("Rat2Walk"),
    Rat2Idle("Rat2Idle"),
    Rat2Hurt("Rat2Hurt"),
    Rat2Death("Rat2Death"),
    Birt1Walk("Birt1Walk"),
    Birt1Idle("Birt1Idle"),
    Birt1Hurt("Birt1Hurt"),
    Birt1Death("Birt1Death"),
    Birt2Walk("Birt2Walk"),
    Birt2Idle("Birt2Idle"),
    Birt2Hurt("Birt2Hurt"),
    Birt2Death("Birt2Death"),
    Enemy1Walk("Enemy1Walk"),
    Enemy1Idle("Enemy1Idle"),
    Enemy1Hurt("Enemy1Hurt"),
    Enemy1Death("Enemy1Death"),
    Enemy1Attack("Enemy1Attack"),
    Enemy2Walk("Enemy2Walk"),
    Enemy2Idle("Enemy2Idle"),
    Enemy2Hurt("Enemy2Hurt"),
    Enemy2Death("Enemy2Death"),
    Enemy2Attack("Enemy2Attack"),
    Enemy3Walk("Enemy3Walk"),
    Enemy3Idle("Enemy3Idle"),
    Enemy3Hurt("Enemy3Hurt"),
    Enemy3Hurt1("Enemy3Hurt1"),
    Enemy3Death("Enemy3Death"),
    Enemy3Attack("Enemy3Attack"),
    Enemy4Walk("Enemy4Walk"),
    Enemy4Idle("Enemy4Idle"),
    Enemy4Hurt("Enemy4Hurt"),
    Enemy4Death("Enemy4Death"),
    Enemy4Attack("Enemy4Attack"),
    Enemy5Walk("Enemy5Walk"),
    Enemy5Idle("Enemy5Idle"),
    Enemy5Hurt("Enemy5Hurt"),
    Enemy5Death("Enemy5Death"),
    Enemy5Attack("Enemy5Attack"),
    Enemy6Attack("Enemy6Attack"),
    Enemy6Walk("Enemy6Walk"),
    Enemy6Idle("Enemy6Idle"),
    Enemy6Hurt("Enemy6Hurt"),
    Enemy6Death("Enemy6Death"),
    Boss1Attack4("Boss1Attack4"),
    Boss1Attack3("Boss1Attack3"),
    Boss1Attack2("Boss1Attack2"),
    Boss1Attack1("Boss1Attack1"),
    Boss1Sneer("Boss1Sneer"),
    Boss1Idle("Boss1Idle"),
    Boss1Hurt("Boss1Hurt"),
    Boss1Death("Boss1Death"),
    Boss1Walk("Boss1Walk"),
    Boss2Attack4("Boss2Attack4"),
    Boss2Attack2("Boss2Attack2"),
    Boss2Attack3("Boss2Attack3"),
    Boss2Attack1("Boss2Attack1"),
    Boss2Sneer("Boss2Sneer"),
    Boss2Idle("Boss2Idle"),
    Boss2Hurt("Boss2Hurt"),
    Boss2Death("Boss2Death"),
    Boss2Walk("Boss2Walk"),
    BikerIdleGun("BikerIdleGun"),
    BikerJumpGun("BikerJumpGun"),
    BikerRunGun("BikerRunGun"),
    PunkIdleGun("PunkIdleGun"),
    PunkRunGun("PunkRunGun"),
    PunkJumpGun("PunkJumpGun"),
    CyborgIdleGun("CyborgIdleGun"),
    CyborgRunGun("CyborgRunGun"),
    CyborgJumpGun("CyborgJumpGun"),
    Helicopter("Helicopter"),
    Airplane("Airplane"),
    AirplaneBomb("AirplaneBomb"),

    BikerThrow("BikerThrow"),
    CyberThrow("CyberThrow"),
    PunkThrow("PunkThrow"),
    Bomb1("Bomb1"),
    Bomb2("Bomb2"),
    Bomb3("Bomb3"),
    Bomb4("Bomb4");
    private final String stringValue;

    AnimationType(String s) {
        stringValue = s;
    }

    public String getStringValue() {
        return stringValue;
    }
}
