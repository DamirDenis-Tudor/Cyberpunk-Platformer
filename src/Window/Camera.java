package Window;

import Utils.Constants;
import Utils.Coordinate;

/**
 * This class describes the basic functionality of a camera that focuses on a component
 */
public class Camera {

    /**
     * Shared instance.
     */
    private static Camera instance;

    /**
     * Reference to component focused position.
     */
    private Coordinate<Integer> focusComponentPosition;

    /**
     * Stores the horizontal offsets: X - current , Y - past
     */
    private final Coordinate<Integer> horizontalOffsets;

    /**
     * Stores the vertical offsets: X - current , Y - past
     */
    private final Coordinate<Integer> verticalOffsets;

    /**
     * Stores the map dimensions: X - width , Y - height
     */
    private final Coordinate<Integer> mapDimensions;

    /**
     * Stores the current state of camera.
     */
    private boolean active = true;

    /**
     * This constructor is called once at the installation of static instance.
     */
    private Camera() {
        horizontalOffsets = new Coordinate<>(0, 0);
        verticalOffsets = new Coordinate<>(0, 0);
        focusComponentPosition = new Coordinate<>(0, 0);
        mapDimensions = new Coordinate<>(0, 0);
    }

    /**
     * Getter for the static instance.
     *
     * @return shared instance of class
     */
    public static Camera get() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    /**
     * @return current calculated horizontal offset.
     */
    public int getCurrentHorizontalOffset() {
        return horizontalOffsets.getPosX();
    }

    /**
     * @return previous calculated horizontal offset.
     */
    public int getPastHorizontalOffset() {
        return horizontalOffsets.getPosY();
    }

    /**
     * @return current calculated vertical offset.
     */
    public int getCurrentVerticalOffset() {
        return verticalOffsets.getPosX();
    }

    /**
     * This method enables the effects of camera offsets.
     * For example, when a transition is made from play to a menu-related scene, this method is called.
     */
    public void disableCameraOffset() {
        horizontalOffsets.setX(0);
        verticalOffsets.setX(0);
        active = false;
    }

    /**
     * This method disables the effects of camera offsets.
     * For example, when a transition is made from a menu-related scene to play, this method is called.
     */
    public void enableCurrentOffset() {
        horizontalOffsets.setX(horizontalOffsets.getPosY());
        verticalOffsets.setX(verticalOffsets.getPosY());
        active = true;
    }

    /**
     * @param component reference to a focused component
     */
    public void setFocusComponentPosition(Coordinate<Integer> component) {
        this.focusComponentPosition = component;
    }

    /**
     * This method refreshes the current camera horizontal boundary.
     *
     * @param dimension map width dimension
     */
    public void setGameMapPixelWidthDimension(int dimension) {
        mapDimensions.setX(dimension);
    }

    /**
     * This method refreshes the current camera vertical boundary.
     *
     * @param dimension map height dimension
     */
    public void setGameMapPixelHeightDimension(int dimension) {
        mapDimensions.setY(dimension);
    }

    /**
     * This method is responsible for detecting the borders of the map and to stop camera movement when is necessary.
     */
    public void update() {
        if (active) {
            // Calculating horizontal offset.
            if (focusComponentPosition.getPosX() > Constants.WINDOW_WIDTH / 2 &&
                    focusComponentPosition.getPosX() < mapDimensions.getPosX() - Constants.WINDOW_WIDTH / 2) {
                horizontalOffsets.setY(horizontalOffsets.getPosX());
                horizontalOffsets.setX(-focusComponentPosition.getPosX() + Constants.WINDOW_WIDTH / 2);
            } else if (focusComponentPosition.getPosX() <= Constants.WINDOW_WIDTH / 2) {
                horizontalOffsets.setX(0);
                horizontalOffsets.setY(0);
            } else if (focusComponentPosition.getPosX() >= mapDimensions.getPosX() - Constants.WINDOW_WIDTH / 2) {
                horizontalOffsets.setX(horizontalOffsets.getPosY());
            }

            // Calculating vertical offset.
            if (focusComponentPosition.getPosY() <= Constants.WINDOW_HEIGHT / 2) {
                verticalOffsets.setX(0);
            } else if (focusComponentPosition.getPosY() > Constants.WINDOW_HEIGHT / 2 &&
                    focusComponentPosition.getPosY() < mapDimensions.getPosY() - Constants.WINDOW_HEIGHT / 2) {
                verticalOffsets.setX(-focusComponentPosition.getPosY() + Constants.WINDOW_HEIGHT / 2);
            } else if (focusComponentPosition.getPosY() >= mapDimensions.getPosY() - Constants.WINDOW_HEIGHT / 2) {
                verticalOffsets.setX(-mapDimensions.getPosY() + Constants.WINDOW_HEIGHT);
            }
        }
    }
}