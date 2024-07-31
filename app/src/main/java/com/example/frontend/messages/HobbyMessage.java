package com.example.frontend.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HobbyMessage extends BaseMessage {
    public int id;
    public String title;
    public int number;

    public HobbyMessage(){
        this.messageType = MessageType.HOBBY;
    }
}
