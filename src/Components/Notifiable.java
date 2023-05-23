package Components;

import Exceptions.ComponentNotFoundException;
import Scenes.Messages.Message;

/**
 * This interface gives the option to receive messages.
 */
public interface Notifiable {
    void notify(Message message);
}
