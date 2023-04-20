package Utils;

import java.io.Serializable;

/**
 * This class is helpful in the position referencing.
 * Example: camera takes a reference to the player position and so on.
 * @param <T> must extend Number
 */
public class Coordinate <T extends Number> implements Serializable {
    private T posX ;
    private T posY ;

    public Coordinate(T posX , T posY){
        this.posX = posX;
        this.posY = posY;
    }

    public Coordinate(Coordinate<T> other){
        this.posX = other.posX;
        this.posY = other.posY;
    }
    public void setX(T posX){this.posX = posX;}
    public void setY(T posY){
        this.posY = posY;
    }
    public T getPosX(){return posX;}
    public T getPosY(){
        return posY;
    }

    public int distance(Coordinate<T> other) {
        int deltaX = getPosX().intValue() - other.getPosX().intValue();
        int deltaY = getPosY().intValue() - other.getPosY().intValue();
        return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    @Override
    public String toString() {
        return "X : " + posX + " , Y : " + posY;
    }
}
