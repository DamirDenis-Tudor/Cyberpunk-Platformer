package Components;

import Scenes.Messages.Message;

/**
 * This interface gives the option to receive messages.
 *
 * @see Message
 */
public interface Notifiable {
    void notify(Message message);
}
