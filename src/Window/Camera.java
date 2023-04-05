package Window;

import Utils.Coordinate;

/**
 * This class describes the basic the functionality
 * of a camera.
 */
public class Camera {
    private static Camera instance;
    private final GameWindow gameWindow;
    private Coordinate<Integer> focusComponentPosition;
    private int gameMapPixelDimension = 0;
    private int currentOffset = 0;
    private int pastOffset = 0;

    private Camera() {
        gameWindow = GameWindow.getInstance();
        focusComponentPosition = new Coordinate<>(0, 0);
    }

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public int getPastOffset() {
        return pastOffset;
    }

    public void setFocusComponentPosition(Coordinate<Integer> component) {
        this.focusComponentPosition = component;
    }

    public void setGameMapPixelDimension(int dimension){
        gameMapPixelDimension = dimension;
    }

    public void update() {
        if (focusComponentPosition.getPosX() > gameWindow.GetWndWidth() / 2 &&
                focusComponentPosition.getPosX() < gameMapPixelDimension - gameWindow.GetWndWidth() / 2) {
            pastOffset = currentOffset;
            currentOffset = -focusComponentPosition.getPosX() + gameWindow.GetWndWidth()/2 ;
        }else {
            currentOffset = pastOffset;
        }
    }
}
