package com.example.frontend.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.frontend.R
import com.example.frontend.fragments.HobbyFragment
import com.example.frontend.fragments.LocationFragment
import com.example.frontend.fragments.ProfileFragment
import com.example.frontend.fragments.SettingsFragment
import com.example.frontend.messages.HobbyMessage
import com.example.frontend.messages.MessageType
import com.example.frontend.messages.TestMessage
import com.example.frontend.networking.WebSocketClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    var networkHandler: WebSocketClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkHandler = WebSocketClient()
        connectToWebSocketServer()
        sendMessage()

        // BottomNavigationView setup
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    openFragment(HobbyFragment())
                    true
                }
                R.id.navigation_location -> {
                    openFragment(LocationFragment())
                    true
                }
                R.id.navigation_settings -> {
                    openFragment(SettingsFragment())
                    true
                }
                R.id.navigation_profile -> {
                    openFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }


        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.navigation_home
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun connectToWebSocketServer() {
        networkHandler!!.addMessageHandler(MessageType.TEST.toString(), this::messageReceivedFromServer)
        networkHandler!!.connectToServer()
    }

    private fun sendMessage() {
        val testMessage = TestMessage()
        testMessage.text = "test message"

        val jsonMessage = gson.toJson(testMessage)
        networkHandler!!.sendMessageToServer(jsonMessage)
    }

    private fun <T> messageReceivedFromServer(message: T) {
        if (message is String) {
            val jsonString = message
        }
    }
}
