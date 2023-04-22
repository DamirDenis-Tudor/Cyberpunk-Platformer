package Utils;

public record Constants() {
    public static final float mapScale = 2f;
    public static final float imageScale = 1.48148f;
    public static final int mapDim = (int)(32 * mapScale);
    public static final int gravitationForce = 12;
    public static final int playerVelocity = 6;
    public static final int baseballEnemyVelocity = 3;
    public static final int animalEnemyVelocity = 3;
    public static final int skaterEnemyVelocity = 4;
    public static final int gunnarEnemyVelocity = 3;
    public static final int platformVelocity = 3;
    public static final int helicopterVelocity = 4;
    public static final int bulletVelocity = 25;
    public static final int enemyRange = 10 * mapDim;
    public static final int bulletMaxRange = 20 * mapDim;

}


