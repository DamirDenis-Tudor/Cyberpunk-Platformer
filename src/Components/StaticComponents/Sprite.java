package Components.StaticComponents;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite implements StaticComponent {
    private final BufferedImage tile;
    private int xPosition;
    private int yPosition;

    public Sprite(String path) throws IOException {
        tile = ImageIO.read(new File(path));
        xPosition = 0;
        yPosition = 0;
    }

    public Sprite(Sprite copy){
        this.tile = copy.tile;
        xPosition = 0;
        yPosition = 0;
    }

    /**
     *
     * @param xPosition
     * @param yPosition
     */
    public void setPosition(int xPosition , int yPosition){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    @Override
    public void draw() {

    }

    public BufferedImage getTile(){
        return tile;
    }

    @Override
    public void update() {

    }
}
