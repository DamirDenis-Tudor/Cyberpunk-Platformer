package Components.DinamicComponents.Items;

import Components.DinamicComponents.DynamicComponent;
import Enums.ComponentType;
import Scenes.Messages.Message;
import Utils.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends DynamicComponent {
    private final BufferedImage image;

    public Bullet(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.image = image;
    }

    @Override
    public void notify(Message message) throws Exception {

    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {

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
