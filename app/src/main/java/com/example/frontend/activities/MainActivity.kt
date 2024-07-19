package com.example.frontend.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.example.frontend.messages.MessageType
import com.example.frontend.messages.TestMessage
import com.example.frontend.networking.WebSocketClient
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    var networkHandler: WebSocketClient? = null
    private val gson = Gson()
    /*
    var dbHandler : Database_Helper?= null
    var hobby : HobbyModel? = null
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        dbHandler = Database_Helper(this)

        hobby = HobbyModel().apply {
            id = 1
            user = "John Doe"
            title = "Hiking"
            description = "Exploring new trails"
            number = 5
            date = "2023-03-15"
            location = "Mount Everest"
            latitude = 27.9881
            longitude = 86.9250
        }

        addHobby(hobby!!)
        */

        networkHandler = WebSocketClient()
        connectToWebSocketServer()
        sendMessage()
    }
    /*
    private fun addHobby(hobby: HobbyModel){
        dbHandler!!.addHobby(hobby)
    }
    */

    private fun connectToWebSocketServer() {
        networkHandler!!.addMessageHandler(MessageType.TEST.toString(), this::messageReceivedFromServer)
        networkHandler!!.connectToServer()
    }

    private fun sendMessage() {
        val testMessage: TestMessage = TestMessage()
        testMessage.text = "test message"

        val jsonMessage: String = gson.toJson(testMessage)
        networkHandler!!.sendMessageToServer(jsonMessage)
        Log.d("Network", "to server: $jsonMessage")
    }

    private fun <T> messageReceivedFromServer(message: T) {
        if (message is String) {
            val jsonString = message
            Log.d("Network", "from server: $jsonString")
        }
        else {
            Log.e("Error", "Received message is not a String")
        }
    }
}