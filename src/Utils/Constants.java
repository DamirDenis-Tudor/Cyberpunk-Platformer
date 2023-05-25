package Utils;

import java.awt.*;

/**
 * This class contains a series of predefined values.
 */
public record Constants() {

    public static final int WINDOW_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    public static final int WINDOW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final float MAP_SCALE = 2f;
    public static final float IMAGE_SCALE = 1.48148f;
    public static final int MAP_DIM = (int) (32 * MAP_SCALE);
    public static final int GRAVITATION_FORCE = 12;
    public static final int PLAYER_VELOCITY = 6;
    public static final int BASEBALL_ENEMY_VELOCITY = 3;
    public static final int ANIMAL_ENEMY_VELOCITY = 3;
    public static final int SKATER_ENEMY_VELOCITY = 4;
    public static final int GUNNAR_ENEMY_VELOCITY = 3;
    public static final int PLATFORM_VELOCITY = 3;
    public static final int HELICOPTER_VELOCITY = 4;
    public static final int AIRPLANE_VELOCITY = 2;
    public static final int DRONE_VELOCITY = 5;
    public static final int DRONE_BOOTS_VELOCITY = 8;
    public static final int BULLET_VELOCITY = 25;
    public static final int ENEMY_RANGE = 10 * MAP_DIM;
    public static final int BULLET_MAX_RANGE = 20 * MAP_DIM;
    public static final int AIRPLANE_MAX_RANGE = 10 * MAP_DIM;
    public static final int AIRPLANE_DROP_BOMB_RANGE = 10 * MAP_DIM;
    public static final int DRONE_MAX_RANGE = 8 * MAP_DIM;
    public static final int MAX_ITEMS_NUMBER = 5;
    public static final int FIRST_ITEM = 1;
    public static final int INVALID_ID = -1;

}


