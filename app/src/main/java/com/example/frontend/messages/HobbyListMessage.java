package com.example.frontend.messages;

import com.example.frontend.models.HobbyModel;

import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HobbyListMessage extends BaseMessage {
    public Set<HobbyModel> hobbies;

    public HobbyListMessage() {
        this.messageType = MessageType.HOBBYLIST;
    }
}
