package Utils;

public record Constants() {
    public static final float mapScale = 1.6875f;
    public static final int mapDim = (int)(32 * mapScale);
    public static final int gravitationForce = 10;
    public static final int playerVelocity = 5;
    public static final int baseballEnemyVelocity = 1;
    public static final int animalEnemyVelocity = 2;
    public static final int skaterEnemyVelocity = 3;
    public static final int gunnarEnemyVelocity = 2;
    public static final int platformVelocity = 2;
    public static final int helicopterVelocity = 3;
    public static final int enemyRange = 10 * mapDim;

}


