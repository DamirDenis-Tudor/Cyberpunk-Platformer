package Utils;

import java.io.Serializable;

/**
 * This class is important for each object of the game, because it can be attributed to any component in the game.
 * The most important aspect is that it has a built-in mechanism for collision detection and solving.
 */
public class Rectangle implements Serializable {
    /**Width of rectangle.*/
    private int width;

    /**Height of rectangle.*/
    private int height;

    /**Position of the rectangle.*/
    private Coordinate<Integer> position;

    /**Offsets of intersection*/
    private double dx = 0.0, dy = 0.0;

    /**
     * Constructor for rectangle.
     * @param position start position.
     * @param width start width.
     * @param height start height.
     */
    public Rectangle(Coordinate<Integer> position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    /**
     * Copy constructor for rectangle.
     * @param other to be copied.
     */
    public Rectangle(Rectangle other) {
        this.position = new Coordinate<>(other.position);
        this.width = other.width;
        this.height = other.height;
        this.dx = other.dx;
        this.dy = other.dy;
    }

    /**
     * Getter for height
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for width
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for position.
     * @return position
     */
    public Coordinate<Integer> getPosition() {
        return position;
    }

    /**
     * Setter for width.
     * @param value new width.
     */
    public void setWidth(int value){
        width = value;
    }

    /**
     * Setter for height.
     * @param value new height.
     */
    public void setHeight(int value){
        height = value;
    }
    /**
     * Setter for position
     * @param position new position.
     */
    public void setPosition(Coordinate<Integer> position) {this.position = position;}

    /**
     * Left corner horizontal position.
     * @return x position
     */
    public Integer getMinX() {
        return position.getPosX();
    }

    /**
     * Right corner horizontal position.
     * @return x position
     */
    public Integer getMaxX() {return position.getPosX() + width;}

    /**
     * Top corner vertical position.
     * @return y position
     */
    public Integer getMinY() {return position.getPosY();}

    /**
     * Down corner vertical position.
     * @return y position
     */
    public Integer getMaxY() {return position.getPosY() + height;}

    /**
     * Center horizontal position.
     * @return x position.
     */
    public Integer getCenterX() {return position.getPosX() + width / 2;}

    /**
     * Center vertical position.
     * @return y position.
     */
    public Integer getCenterY() {return position.getPosY() + height / 2;}

    /**
     * Getter for a center copy position.
     * @return coordinate position.
     */
    public Coordinate<Integer> getCopyCenteredPosition(){
        return new Coordinate<>(getCenterX() , getCenterY());
    }

    /**
     * This method moves the rectangle horizontally.
     * @param x distance
     */
    public void moveByX(int x) {position.setX(position.getPosX() + x);}

    /**
     * This method moves the rectangle vertically.
     * @param y distance
     */
    public void moveByY(int y) {position.setY(position.getPosY() + y);}

    /**
     * This method calculates the distance between two rectangles.
     * @param other focus rectangle.
     * @return distance between rectangles centers.
     */
    public int calculateDistanceWith(Rectangle other){return getCopyCenteredPosition().distance(other.getCopyCenteredPosition());}

    /**
     * This method verifies if two rectangles intersect and save the offsets.
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

    /**
     * Verifies if a point is within a rectangle.
     * @param point coordinate.
     * @return intersection status.
     */
    public boolean contains(Coordinate<Integer> point){return point.getPosX() < getMaxX() && point.getPosX() > getMinX() && point.getPosY() < getMaxY() && point.getPosY() > getMinY();}

    /**
     * Getter for horizontal offset.
     * @return x offset
     */
    public double getDx() {
        return dx;
    }

    /**
     * Getter for vertical offset.
     * @return y offset
     */
    public double getDy() {
        return dy;
    }

    @Override
    public String toString() {
        return position.toString() + " , Width : " + width + " , Height : " + height + "\n";
    }
}
