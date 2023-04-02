package Utils;

public class Rectangle {
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

    public Integer getMaxX() {
        return position.getPosX() + width;
    }

    public Integer getMinY() {
        return position.getPosY();
    }

    public Integer getMaxY() {
        return position.getPosY() + height;
    }

    public Integer getCenterX() {
        return position.getPosX() + width / 2;
    }

    public Integer getCenterY() {
        return position.getPosY() + height / 2;
    }

    public void setPosition(Coordinate<Integer> position) {
        this.position = position;
    }

    public void moveByX(int x) {
        position.setX(position.getPosX() + x);
    }

    public void moveByY(int y) {
        position.setY(position.getPosY() + y);
    }

    public boolean intersects(Rectangle other) {
        // check for overlap in the x-direction
        double x_overlap = Math.max(0, Math.min(getMaxX(), other.getMaxX()) - Math.max(getMinX(), other.getMinX()));

        // check for overlap in the y-direction
        double y_overlap = Math.max(0, Math.min(getMaxY(), other.getMaxY()) - Math.max(getMinY(), other.getMinY()));

        if (x_overlap * y_overlap > 0) {

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
     * This method solves the collision with another rectangle.
     *
     * @param other rectangle
     * @return vertical offset :<br><br/>
     * negative -> bottom collision with "other" rectangle <br><br/>
     * positive -> top collision with "other" rectangle
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
