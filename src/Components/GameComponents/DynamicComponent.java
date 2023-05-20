package Components.GameComponents;

import Components.Interactive;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.ComponentType;
import Scenes.Scene;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;

import java.io.Serializable;

/**
 * This allows for the updating and drawing of any component, the ability to make requests
 * to the associated scene, and to handle interactions with other objects in a specific manner.
 * Additionally, it provides the capability for the object to be recognized by an identifier.
 * As a result, access to the physical object is not necessary as the object can be identified solely by its identifier.
 * @see StaticComponent
 * @see Interactive
 * @see Notifiable
 * @see Serializable
 */
public abstract class DynamicComponent implements StaticComponent, Interactive , Notifiable, Serializable {
    private static int idCounter = 0;
    private int id=0;
    private boolean active = true;

    transient protected Notifiable scene = null;

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

    public void incrementIdCounter(){
        idCounter++;
    }

    public static void setIdCounter(int idCounter) {
        DynamicComponent.idCounter = idCounter;
    }
    public Rectangle getCollideBox() {
        return collideBox;
    }
    public abstract ComponentType getCurrentType();
    public abstract ComponentType getGeneralType();

    @Override
    public void update(){
        if(collideBox == null) return;

        Rectangle window = new Rectangle(
                new Coordinate<>(Math.max(0, -Camera.get().getCurrentHorizontalOffset()),  Math.max(0, -Camera.get().getCurrentVerticalOffset()))
                        , Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        active = collideBox.intersects(window);
    }
    public void addMissingPartsAfterDeserialization(Notifiable scene){
        this.scene = scene;

        // restore the exact counter id as that from the saved state
        // this is accomplished when the last component from the save is deserialized.
        setIdCounter(getId());
        incrementIdCounter();
    };
}
