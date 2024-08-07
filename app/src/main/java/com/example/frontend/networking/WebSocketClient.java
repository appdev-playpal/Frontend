package com.example.frontend.networking;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient {

    private final String WEBSOCKET_URI = "ws://10.0.2.2:8080/websocket-example-handler";

    private WebSocket webSocket;
    private Map<String, WebSocketMessageHandler<String>> messageHandlers;

    public WebSocketClient() {
        messageHandlers = new HashMap<>();
    }

    public void connectToServer() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(WEBSOCKET_URI)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                Log.d("Network", "connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(text, JsonElement.class);
                String type = jsonElement.getAsJsonObject().get("messageType").getAsString();

                WebSocketMessageHandler<String> handler = messageHandlers.get(type);
                if (handler!= null) {
                    handler.onMessageReceived(text);
                } else {
                    Log.d("Network", "Unknown message type: " + type);
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // WebSocket connection closed
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                // Permission needed to transmit cleartext in the manifest
                Log.d("Network", "connection failure", t);
            }
        });
    }

    public void addMessageHandler(String messageType, WebSocketMessageHandler<String> handler) {
        messageHandlers.put(messageType, handler);
    }

    public void sendMessageToServer(String msg) {
        if (webSocket!= null) {
            webSocket.send(msg);
        } else {
            Log.d("Network", "WebSocket is not open, can't send a message");
        }
    }
}
