package Components.GameItems;

import Components.Interactive;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.ComponentType;
import Scenes.Scene;
import Utils.Rectangle;

import java.io.Serializable;

/**
 * This allows for the updating and drawing of any component, the ability to make requests
 * to the associated scene, and to handle interactions with other objects in a specific manner.
 * Additionally, it provides the capability for the object to be recognized by an identifier.
 * As a result, access to the physical object is not necessary as the object can be identified solely by its identifier.
 */
public abstract class DynamicComponent implements StaticComponent, Interactive , Notifiable, Serializable {
    private static int idCounter = 0;
    private int id=0;
    private boolean active = true;

    transient protected Scene scene = null;

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

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public int getId(){
        return id;
    }
    public Rectangle getCollideBox() {
        return collideBox;
    }
    public abstract ComponentType getCurrentType();
    public abstract ComponentType getGeneralType();
    public abstract void addMissingPartsAfterDeserialization(Scene scene);
}
