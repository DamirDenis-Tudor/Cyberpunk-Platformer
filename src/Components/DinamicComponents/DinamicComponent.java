package Components.DinamicComponents;

import Enums.ComponentNames;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Rectangle;

/**
 * This allows for the updating and drawing of any component, the ability to make requests
 * to the associated scene, and to handle interactions with other objects in a specific manner.
 * Additionally, it provides the capability for the object to be recognized by an identifier.
 * As a result, access to the physical object is not necessary as the object can be identified solely by its identifier.
 */
public abstract class DinamicComponent{
    protected Scene scene = null;
    protected Rectangle collideBox;
    public abstract void  notify(Message message) throws Exception;
    public abstract void update() throws Exception;
    public abstract void draw();
    public abstract ComponentNames getType();
    public abstract void handleInteractionWith(DinamicComponent component) throws Exception;
    public Rectangle getCollideBox() {
        return collideBox;
    }
}
