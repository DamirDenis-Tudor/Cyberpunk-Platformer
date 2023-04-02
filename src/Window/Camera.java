package Window;

import Input.KeyboardInput;

/**
 * This class describes the basic the functionality
 * of a camera.
 */
public class Camera {
    private static Camera instance ;
    private int currentXoffset=0;

    private int pastOffset =0;
    
    private int pastTargetPosition=0;
    
    private int frameOffset;
    private Camera(){}
    public static Camera getInstance(){
        if(instance == null){
            instance = new Camera();
        }
        return instance;
    }
    public int getCurrentXoffset(){
        return currentXoffset;
    }

    public void setPastTargetPosition(int position){
        pastTargetPosition=position;
    }

    public int getPastOffset(){
        return pastOffset;
    }

    public void setTargetPosition(int x){
        pastOffset = currentXoffset;
        currentXoffset += -x + pastTargetPosition;
        pastTargetPosition = x;
    }
}
