package com.example.frontend.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestMessage extends BaseMessage{
    private String text;
    private String messageIdentifier;

    public TestMessage() {
        this.messageType = MessageType.TEST;
    }
}
