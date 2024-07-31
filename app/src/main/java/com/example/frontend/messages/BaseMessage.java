package com.example.frontend.messages;

public class BaseMessage implements IBaseMessage{
    protected MessageType messageType;

    @Override
    public MessageType getMessageType() { return messageType; }

    public void setMessageType(MessageType messageType) { this.messageType = messageType; }
}
