package Utils;

import java.util.List;

public class Coordinate <T>{
    private T posX ;
    private T posY ;

    public Coordinate(T posX , T posY){
        this.posX = posX;
        this.posY = posY;
    }

    public void setX(T posX){
        this.posX = posX;
    }
    public void setY(T posY){
        this.posY = posY;
    }

    public T getPosX(){
        return posX;
    }
    public T getPosY(){
        return posY;
    }
}
