package Utils;

import java.util.Date;
import java.util.Objects;

import static java.lang.Double.isNaN;

public class Rectangle {
    private int width;
    private int height;
    private Coordinate<Integer> position;

    public Rectangle(Coordinate<Integer> position , int width , int height){
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle other){
        this.position = new Coordinate<Integer>(other.position);
        this.width = other.width;
        this.height = other.height;
    }
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Coordinate<Integer> getPosition() {
        return position;
    }

    public Integer getMinX(){
        return position.getPosX();
    }

    public Integer getMaxX(){
        return position.getPosX() + width;
    }

    public Integer getMinY(){
        return position.getPosY();
    }

    public Integer getMaxY(){
        return position.getPosY() + height;
    }
    public Integer getCenterX(){
        return position.getPosX() + width/2;
    }
    public Integer getCenterY(){
        return position.getPosY() + height/2;
    }
    public void setPosition(Coordinate<Integer> position) {
        this.position = position;
    }

    public void moveByX(int x){
        position.setX(position.getPosX()+x);
    }

    public void moveByY(int y){
        position.setY(position.getPosY()+y);
    }

    public boolean intersects(Rectangle other){
        // check for overlap in the x-direction
        double x_overlap = Math.max(0, Math.min(getMaxX(), other.getMaxX()) - Math.max(getMinX(), other.getMinX()));

        // check for overlap in the y-direction
        double y_overlap = Math.max(0, Math.min(getMaxY(), other.getMaxY()) - Math.max(getMinY(), other.getMinY()));

        // check if there is any overlap at all
        return x_overlap * y_overlap > 0;
    }

    /**
     * This method solves the collision with another rectangle.
     * @param other rectangle
     * @return vertical offset :<br><br/>
     * negative -> bottom collision with "other" rectangle <br><br/>
     * positive -> top collision with "other" rectangle
     */
    public double solveCollision(Rectangle other){
        double dx = 0, dy = 0;
        if (this.intersects(other)) {

            // Determine the minimum translation vector
            double x_overlap = Math.min(this.getMaxX(), other.getMaxX()) - Math.max(this.getMinX(), other.getMinX());
            double y_overlap = Math.min(this.getMaxY(), other.getMaxY()) - Math.max(this.getMinY(), other.getMinY());

            if (x_overlap < y_overlap) {
                if (this.getCenterX() < other.getCenterX()) {
                    dx = -x_overlap;
                } else {
                    dx = x_overlap;
                }
            } else {
                if (this.getCenterY() < other.getCenterY()) {
                    dy = -y_overlap;
                } else {
                    dy = y_overlap;
                }
            }

            // recalibration
            this.moveByX((int)dx);
            this.moveByY((int)dy);
           /* if(dx < 0 && !Objects.equals(this.getMaxX(), other.getMinX())){
                this.getPosition().setX(other.getMinX()-this.getWidth());
            }else if( dx > 0 && !Objects.equals(this.getMinX(), other.getMaxX())){
                this.getPosition().setX(other.getMaxX());
            }
            if(dy < 0 && !Objects.equals(this.getMaxY(), other.getMinY())){
                this.getPosition().setY(other.getMinY()-this.getHeight());
            }else if( dy > 0 && !Objects.equals(this.getMinY(), other.getMaxY())){
                this.getPosition().setY(other.getMaxY());
            }*/
        }
        return dy;
    }

    @Override
    public String toString() {
        return position.toString() + " , Width : " + width + " , Height : " + height + "\n";
    }
}
