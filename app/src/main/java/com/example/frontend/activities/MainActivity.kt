package com.example.frontend.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.adapters.HobbyAdapter
import com.example.frontend.fragments.LocationFragment
import com.example.frontend.fragments.ProfileFragment
import com.example.frontend.fragments.SettingsFragment
import com.example.frontend.messages.MessageType
import com.example.frontend.messages.TestMessage
import com.example.frontend.networking.WebSocketClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var hobbyAdapter: HobbyAdapter
    private val hobbies = mutableListOf<Hobby>()
    private val gson = Gson()
    var networkHandler: WebSocketClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView setup
        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        hobbyAdapter = HobbyAdapter(hobbies)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = hobbyAdapter

        // FloatingActionButton to add a new hobby
        findViewById<FloatingActionButton>(R.id.addingBtn).setOnClickListener {
            val intent = Intent(this, AddHobbyActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_HOBBY)
        }

        // BottomNavigationView setup
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    clearBackStack()
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

        networkHandler = WebSocketClient()
        connectToWebSocketServer()
        sendMessage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_HOBBY && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra("title") ?: return
            val numberOfPlayers = data.getIntExtra("numberOfPlayers", 0)
            val hobby = Hobby(title, numberOfPlayers)
            hobbies.add(hobby)
            hobbyAdapter.notifyDataSetChanged()
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            val first = fragmentManager.getBackStackEntryAt(0)
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
    }

    private fun <T> messageReceivedFromServer(message: T) {
        if (message is String) {
            val jsonString = message
            // Handle received message
        }
    }

    companion object {
        const val REQUEST_CODE_ADD_HOBBY = 1
    }
}
