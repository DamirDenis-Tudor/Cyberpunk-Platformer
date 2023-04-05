package Scenes.Messages;

import Enums.ComponentType;
import Enums.MessageType;

public class Message {
    private final MessageType type;
    private final ComponentType source;

    private final int componentId;

    public Message(MessageType type , ComponentType source , int componentId){
        this.type = type;
        this.source = source;
        this.componentId = componentId;
    }

    public MessageType getType(){
        return type;
    }

    public ComponentType getSource(){
        return source;
    }

    public int getComponentId() {
        return componentId;
    }
}
