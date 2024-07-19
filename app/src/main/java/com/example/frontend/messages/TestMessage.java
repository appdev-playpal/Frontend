package com.example.frontend.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestMessage extends BaseMessage{
    public String text;

    public TestMessage() {
        this.messageType = MessageType.TEST;
    }
}
