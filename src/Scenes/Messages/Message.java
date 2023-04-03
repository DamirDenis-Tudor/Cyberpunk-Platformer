package Scenes.Messages;

import Enums.ComponentType;
import Enums.MessageType;

public class Message {
    private MessageType type;
    private ComponentType source;

    public Message(MessageType type , ComponentType source){
        this.type = type;
        this.source = source;
    }

    public MessageType getType(){
        return type;
    }

    public ComponentType getSource(){
        return source;
    }

}
