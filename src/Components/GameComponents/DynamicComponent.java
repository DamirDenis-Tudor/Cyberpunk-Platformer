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
 * This class allows for the updating and drawing of any component, the ability to make requests
 * to the associated scene, and to handle interactions with other objects in a specific manner.
 * Additionally, it provides the capability for the object to be recognized by an identifier.
 * As a result, access to the physical object is not necessary as the object can be identified solely by its identifier.
 *
 * @see StaticComponent
 * @see Interactive
 * @see Notifiable
 * @see Serializable
 */
public abstract class DynamicComponent implements StaticComponent, Interactive, Notifiable, Serializable {
    /**
     * Reference to the component that must be notified.
     */
    transient protected Notifiable scene;

    /**
     * Variable stores the intersection boundaries.
     */
    protected Rectangle collideBox;

    /**
     * Variable stores the type that component is identified by.
     */
    protected ComponentType subtype;

    /**
     * Variable specific to class that gives identifiers to instances.
     */
    private static int idCounter = 0;

    /**
     * Variable that stores the current box id
     */
    private final int id;

    /**
     * Variable that stores the component id.
     */
    private boolean active;

    /**
     * This constructor initializes important fields.
     */
    public DynamicComponent() {
        id = idCounter;
        idCounter++;
        active = true;
        scene = null;
    }

    @Override
    public void update() {
        if (collideBox == null) return;

        Rectangle window = new Rectangle(
                new Coordinate<>(Math.max(0, -Camera.get().getCurrentHorizontalOffset()),
                        Math.max(0, -Camera.get().getCurrentVerticalOffset()))
                , Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        active = collideBox.intersects(window);
    }

    /**
     * This method reconstructs all the content based on the important
     * fields restored after the deserialization.
     *
     * @param scene reference to the component that must be notified.
     */
    public void addMissingPartsAfterDeserialization(Notifiable scene) {
        this.scene = scene;

        // restore the exact counter id as that from the saved state
        // this is accomplished when the last component from the save is deserialized.
        DynamicComponent.idCounter = getId();
        idCounter++;
    }

    /**
     * Getter for the status of the component.
     *
     * @return status of the component
     */
    public boolean getActiveStatus() {
        return active;
    }

    /**
     * Setter for the component status
     *
     * @param value new status
     */
    public void setActiveStatus(boolean value) {
        active = value;
    }

    /**
     * Setter for the component that must be notified.
     *
     * @param scene reference to the component that must be notified.
     */
    public void setScene(Notifiable scene) {
        this.scene = scene;
    }

    /**
     * Getter for the identifier.
     *
     * @return component identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the component collide box.
     *
     * @return intersection box associate to a component.
     */
    public Rectangle getCollideBox() {
        return collideBox;
    }

    /**
     * Abstract getter for the component-specific type.
     *
     * @return component-specific type
     */
    public abstract ComponentType getCurrentType();

    /**
     * Abstract getter for the component-general type.
     *
     * @return component-general type
     */
    public abstract ComponentType getGeneralType();
}
