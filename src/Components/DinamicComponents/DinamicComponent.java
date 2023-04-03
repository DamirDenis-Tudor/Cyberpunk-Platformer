package Components.DinamicComponents;

import Enums.ComponentType;
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
    private static int idCounter = 0;
    private int id=0;
    private boolean active = true;
    public DinamicComponent(){
        id=idCounter;
        idCounter++;
    }
    public int getId(){
        return id;
    }
    public abstract void  notify(Message message) throws Exception;
    public abstract void handleInteractionWith(DinamicComponent component) throws Exception;
    public abstract void update() throws Exception;
    public abstract void draw();
    public abstract ComponentType getType();
    public Rectangle getCollideBox() {
        return collideBox;
    }

    public boolean getActiveStatus(){
        return active;
    }

    public void setActiveStatus(boolean value){
        active = value;
    }
}
