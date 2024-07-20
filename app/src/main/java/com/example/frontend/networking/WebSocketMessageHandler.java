package com.example.frontend.networking;

public interface WebSocketMessageHandler<T> {

    void onMessageReceived(T message);

}
