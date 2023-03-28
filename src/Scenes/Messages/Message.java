package Scenes.Messages;

import Enums.ComponentNames;
import Enums.MessageNames;

public class Message {
    private MessageNames type;
    private ComponentNames source;

    public Message(MessageNames type , ComponentNames source){
        this.type = type;
        this.source = source;
    }

    public MessageNames getType(){
        return type;
    }

    public ComponentNames getSource(){
        return source;
    }

}
