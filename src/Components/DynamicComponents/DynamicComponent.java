package Components.DynamicComponents;

import Components.Interactive;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.ComponentType;
import Scenes.Scene;
import Utils.Rectangle;

/**
 * This allows for the updating and drawing of any component, the ability to make requests
 * to the associated scene, and to handle interactions with other objects in a specific manner.
 * Additionally, it provides the capability for the object to be recognized by an identifier.
 * As a result, access to the physical object is not necessary as the object can be identified solely by its identifier.
 */
public abstract class DynamicComponent implements StaticComponent, Interactive , Notifiable {
    private static int idCounter = 0;
    private int id=0;
    private boolean active = true;

    protected Scene scene = null;

    protected Rectangle collideBox;
    protected ComponentType subtype;
    public DynamicComponent(){
        id=idCounter;
        idCounter++;
    }
    public boolean getActiveStatus(){return active;}
    public void setActiveStatus(boolean value){
        active = value;
    }
    public int getId(){
        return id;
    }
    public Rectangle getCollideBox() {
        return collideBox;
    }
    public abstract ComponentType getCurrentType();
    public abstract ComponentType getGeneralType();
}
