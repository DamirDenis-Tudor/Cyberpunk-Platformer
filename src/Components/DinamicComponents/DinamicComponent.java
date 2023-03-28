package Components.DinamicComponents;

import Scenes.Messages.Message;
import Scenes.Scene;

/**
 * This class brings an additional feature compared to StaticComponent,
 * namely the ability to influence the game's dynamics, more specifically,
 * it can make a request to change a scene.
 *
 * TODO: make it to have the possibility to communicate
 *       with other dynamically objects via a mediator class
 */
public abstract class DinamicComponent{
    protected Scene scene; // reference to the scene that belongs to
    public abstract void notify(Message message);
    public abstract void update() throws Exception;
    public abstract void draw();
}
