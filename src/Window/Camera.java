package Window;

import Utils.Constants;
import Utils.Coordinate;

/**
 * This class describes the basic functionality of a camera that focuses on a component
 */
public class Camera {
    private static Camera instance;
    private final GameWindow gameWindow;
    private Coordinate<Integer> focusComponentPosition; // reference to component position
    private int gameMapPixelWidthDimension = 0; // map width in pixels
    private int gameMapPixelHeightDimension = 0; // map width in pixels

    private boolean active = true;

    private final Coordinate<Integer> horizontalOffsets; // current and past
    private final Coordinate<Integer> verticalOffsets; // current and past
    private int currentOffset = 0; // current frame horizontal offset
    private int pastOffset = 0; // past frame horizontal offset

    /**
     * This constructor is called once at the installation of static instance.
     */
    private Camera() {
        gameWindow = GameWindow.get();
        horizontalOffsets = new Coordinate<>(0,0);
        verticalOffsets = new Coordinate<>(0,0);
        focusComponentPosition = new Coordinate<>(0, 0);
    }

    /**
     *
     * @return shared instance of class
     */
    public static Camera get() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }



    public int getCurrentHorizontalOffset() {
        return horizontalOffsets.getPosX();
    }
    public int getPastHorizontalOffset() {
        return horizontalOffsets.getPosY();
    }
    public int getCurrentVerticalOffset() {
        return verticalOffsets.getPosX();
    }
    public int getPastVerticalOffset() {
        return verticalOffsets.getPosY();
    }


    public void disableCameraOffset(){
        horizontalOffsets.setX(0);
        verticalOffsets.setX(0);
        active = false;
    }

    public void enableCurrentOffset(){
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
    public void setGameMapPixelWidthDimension(int dimension){
        gameMapPixelWidthDimension = dimension;
    }
    public void setGameMapPixelHeightDimension(int dimension){
        gameMapPixelHeightDimension = dimension;
    }

    public int getGameMapPixelHeightDimension() {
        return gameMapPixelHeightDimension;
    }

    public int getGameMapPixelWidthDimension() {
        return gameMapPixelWidthDimension;
    }

    /**
     * This method is responsible for detecting the borders of the map and to stop camera movement when is necessary.
     */
    public void update() {
        if (active) {
            if (focusComponentPosition.getPosX() > Constants.windowWidth / 2 &&
                    focusComponentPosition.getPosX() < gameMapPixelWidthDimension - Constants.windowWidth / 2) {
                horizontalOffsets.setY(horizontalOffsets.getPosX());
                horizontalOffsets.setX(-focusComponentPosition.getPosX() + Constants.windowWidth / 2);
            } else if(focusComponentPosition.getPosX() <= Constants.windowWidth / 2){
                horizontalOffsets.setX(0);
                horizontalOffsets.setY(0);
            } else if (focusComponentPosition.getPosX() >= gameMapPixelWidthDimension - Constants.windowWidth/2) {
                horizontalOffsets.setX(horizontalOffsets.getPosY());
            }

            if(focusComponentPosition.getPosY() <= Constants.windowHeight / 2){
                verticalOffsets.setX(0);
            } else if (focusComponentPosition.getPosY() > Constants.windowHeight / 2 &&
                    focusComponentPosition.getPosY() < gameMapPixelHeightDimension - Constants.windowHeight/2) {
                verticalOffsets.setX(-focusComponentPosition.getPosY() + Constants.windowHeight / 2);
            } else if (focusComponentPosition.getPosY() >= gameMapPixelHeightDimension - Constants.windowHeight/2) {
                verticalOffsets.setX(-gameMapPixelHeightDimension + Constants.windowHeight);
            }
        }
    }
}