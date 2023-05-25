package Components.GameComponents.Map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class is a simple BufferedImage with some dimensions.
 * Its purpose is in Game map class.
 */
public class MapAsset {
    /**
     * Variable that stores the sprite of the asset.
     */
    private final BufferedImage image;

    /**
     * Variable that stores in-game dimensions.
     */
    private final int width,height;

    /**
     * This constructor reads the sprite and its dimensions.
     * @param path where the asset is found.
     * @param width in-game horizontal dimension.
     * @param height in-game vertical dimension.
     * @throws IOException when the image is not found.
     */
    public MapAsset(String path , int width , int height) throws IOException {
        this.width= width;
        this.height = height;

        image = ImageIO.read(new File(path));
    }

    /**
     * Getter for width.
     * @return in-game width.
     */
    public int getWidth(){
        return width;
    }

    /**
     * Getter for height.
     * @return in-game height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for the asset image.
     * @return buffered image sprite.
     */
    public BufferedImage getImage(){
        return image;
    }
}
