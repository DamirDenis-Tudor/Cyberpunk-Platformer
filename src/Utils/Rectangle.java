package Utils;

import java.io.Serializable;

/**
 * This class is important for each object of the game,
 * because it can be attributed to any component in the game.
 * The most important aspect is that it has a built-in mechanism
 * for collision detection and solving.
 */
public class Rectangle implements Serializable {
    private int width;
    private int height;
    private Coordinate<Integer> position;
    private double dx = 0.0, dy = 0.0;

    public Rectangle(Coordinate<Integer> position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle other) {
        this.position = new Coordinate<Integer>(other.position);
        this.width = other.width;
        this.height = other.height;
        this.dx = other.dx;
        this.dy = other.dy;
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

    public Integer getMinX() {
        return position.getPosX();
    }

    public Integer getMaxX() {return position.getPosX() + width;}

    public Integer getMinY() {
        return position.getPosY();
    }

    public Integer getMaxY() {return position.getPosY() + height;}

    public Integer getCenterX() {
        return position.getPosX() + width / 2;
    }

    public Integer getCenterY() {
        return position.getPosY() + height / 2;
    }

    public void setPosition(Coordinate<Integer> position) {this.position = position;}

    public void setWidth(int value){
        width = value;
    }
    public void setHeight(int value){
        height = value;
    }

    public Coordinate<Integer> getCopyCenteredPosition(){
        return new Coordinate<>(getCenterX() , getCenterY());
    }
    public void moveByX(int x) {position.setX(position.getPosX() + x);}

    public void moveByY(int y) {
        position.setY(position.getPosY() + y);
    }

    public int calculateDistanceWith(Rectangle other){
        return getCopyCenteredPosition().distance(other.getCopyCenteredPosition());
    }

    /**
     * This method verifies if two rectangles intersect and save the offsets
     * @param other intersection to be checked with
     * @return intersection status
     */
    public boolean intersects(Rectangle other) {
        // check for overlap in the x-direction
        double x_overlap = Math.max(0, Math.min(getMaxX(), other.getMaxX()) - Math.max(getMinX(), other.getMinX()));

        // check for overlap in the y-direction
        double y_overlap = Math.max(0, Math.min(getMaxY(), other.getMaxY()) - Math.max(getMinY(), other.getMinY()));

        if (x_overlap * y_overlap >= 0) {

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
        }

        // check if there is any overlap at all
        return x_overlap * y_overlap > 0;
    }

    /**
     * This method solves the collision with another rectangle
     * @param other collision to be solved with
     */
    public void solveCollision(Rectangle other) {
        dx = 0;
        dy = 0;

        if (intersects(other)) {
            // recalibration
            this.moveByX((int) dx);
            this.moveByY((int) dy);
        }
    }

    public boolean contains(Coordinate<Integer> point){
        return point.getPosX() < getMaxX() && point.getPosX() > getMinX() &&
                point.getPosY() < getMaxY() && point.getPosY() > getMinY();
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    @Override
    public String toString() {
        return position.toString() + " , Width : " + width + " , Height : " + height + "\n";
    }
}
