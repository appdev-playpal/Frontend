package com.example.frontend.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.frontend.R
import com.example.frontend.fragments.LocationFragment
import com.example.frontend.fragments.ProfileFragment
import com.example.frontend.fragments.SettingsFragment
import com.example.frontend.messages.MessageType
import com.example.frontend.messages.TestMessage
import com.example.frontend.networking.WebSocketClient
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        // Create instances of the fragments that will be used
        val locationFragment = LocationFragment()
        val settingsFragment = SettingsFragment()
        val profileFragment = ProfileFragment()

        // Get a reference to the BottomNavigationView from the layout
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)

        // Set a listener to handle item selection events in the BottomNavigationView
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    clearBackStack()
                    true
                }

                R.id.navigation_location -> {
                    openFragment(locationFragment)
                    true
                }

                R.id.navigation_settings -> {
                    openFragment(settingsFragment)
                    true
                }

                R.id.navigation_profile -> {
                    openFragment(profileFragment)
                    true
                }

                else -> false // Return false if no valid item is selected
            }
        }
    }
    /*
    private fun addHobby(hobby: HobbyModel){
        dbHandler!!.addHobby(hobby)
    }
    */

    // Function to replace the current fragment with a new one
    private fun openFragment(fragment: Fragment) {
        // Begin a new fragment transaction
        val transaction = supportFragmentManager.beginTransaction()
        // Replace the content of the fragment container with the new fragment
        transaction.replace(R.id.fragment_container, fragment)
        // Add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null)
        // Commit the transaction to apply the changes
        transaction.commit()
    }

    // Function to clear the back stack of fragment transactions
    private fun clearBackStack() {
        // Get the FragmentManager that handles fragment transactions
        val fragmentManager = supportFragmentManager
        // Check if there are any fragments in the back stack
        if (fragmentManager.backStackEntryCount > 0) {
            // Get the first fragment entry in the back stack
            val first = fragmentManager.getBackStackEntryAt(0)
            // Pop the back stack entries up to and including the first entry
            fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

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