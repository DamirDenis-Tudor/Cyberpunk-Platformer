package Components.DinamicComponents.Items;

import Components.DinamicComponents.DinamicComponent;
import Enums.ComponentType;
import Scenes.Messages.Message;
import Utils.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gun extends DinamicComponent {
    private final BufferedImage image;

    public Gun(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.image = image;
    }

    @Override
    public void notify(Message message) throws Exception {

    }

    @Override
    public void handleInteractionWith(DinamicComponent component) throws Exception {

    }

    @Override
    public void update() throws Exception {

    }

    @Override
    public void draw() {

    }

    @Override
    public ComponentType getType() {
        return null;
    }
}
