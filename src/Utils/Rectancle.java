package Utils;

import java.util.Objects;

public class Rectancle {
    private int width;
    private int height;
    private Coordinate<Integer> position;

    public Rectancle(Coordinate<Integer> position , int width , int height){
        this.position = position;
        this.width = width;
        this.height = height;
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

    public boolean intersects(Rectancle other){
        // check for overlap in the x-direction
        double x_overlap = Math.max(0, Math.min(getMaxX(), other.getMaxX()) - Math.max(getMinX(), other.getMinX()));

        // check for overlap in the y-direction
        double y_overlap = Math.max(0, Math.min(getMaxY(), other.getMaxY()) - Math.max(getMinY(), other.getMinY()));

        // check if there is any overlap at all
        return x_overlap * y_overlap > 0;
    }

    public void solveCollision(Rectancle other){
        if (this.intersects(other)) {
            // Determine the minimum translation vector
            double dx = 0, dy = 0;
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
            this.moveByX((int)dx);
            this.moveByY((int)dy);
        }
    }

    @Override
    public String toString() {
        return position.toString() + " , Width : " + width + " , Height : " + height + "\n";
    }
}
