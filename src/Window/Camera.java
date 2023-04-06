package Window;

import Utils.Coordinate;

/**
 * This class describes the basic functionality of a camera that focuses on a component
 */
public class Camera {
    private static Camera instance;
    private final GameWindow gameWindow;
    private Coordinate<Integer> focusComponentPosition; // reference to component position
    private int gameMapPixelDimension = 0; // map width in pixels
    private int currentOffset = 0; // current frame horizontal offset
    private int pastOffset = 0; // past frame horizontal offset

    /**
     * This constructor is called once at the installation of static instance.
     */
    private Camera() {
        gameWindow = GameWindow.getInstance();
        focusComponentPosition = new Coordinate<>(0, 0);
    }

    /**
     *
     * @return shared instance of class
     */
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

    /**
     * @param component reference to focused component
     */
    public void setFocusComponentPosition(Coordinate<Integer> component) {
        this.focusComponentPosition = component;
    }

    public void setGameMapPixelDimension(int dimension){
        gameMapPixelDimension = dimension;
    }


    /**
     * This method is responsible for detecting the borders of the map and to stop camera movement when is necessary.
     */
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
