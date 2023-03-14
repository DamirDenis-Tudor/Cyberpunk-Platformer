package GameWindow;

import Input.KeyboardInput;

/**
 * This class describes the basic the functionality
 * of a camera.
 * @note camera movement in only horizontally and must be
 *       focused explicitly on a Player Object
 * @see Components.DinamicComponents.Characters.Player
 */
public class Camera {
    private static Camera instance ;
    private int currentXoffset;
    private int pastXoffset;

    private Camera(){
        currentXoffset = 0;
    }
    public static Camera getInstance(){
        if(instance == null){
            instance = new Camera();
        }
        return instance;
    }

    public int getCurrentXoffset(){
        return currentXoffset;
    }

    public void setCurrentXoffset(int offset){
        currentXoffset += offset;
    }
}
