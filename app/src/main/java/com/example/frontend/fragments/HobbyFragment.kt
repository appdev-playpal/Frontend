package com.example.frontend.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.activities.AddHobbyActivity
import com.example.frontend.activities.HobbyAdapter
import com.example.frontend.database.Database_Helper
import com.example.frontend.messages.HobbyListMessage
import com.example.frontend.messages.HobbyMessage
import com.example.frontend.messages.MessageType
import com.example.frontend.models.HobbyModel
import com.example.frontend.networking.WebSocketClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

@Suppress("DEPRECATION")
class HobbyFragment : Fragment() {

    private val gson = Gson()
    var networkHandler: WebSocketClient? = null
    private var hobbyAdapter: HobbyAdapter? = null
    private val hobbies = ArrayList<HobbyModel>()

    companion object {
        const val REQUEST_CODE_ADD_HOBBY = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hobby, container, false)

        // RecyclerView setup
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        hobbyAdapter = HobbyAdapter(hobbies)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = hobbyAdapter

        // FloatingActionButton to add a new hobby
        val addButton = view.findViewById<FloatingActionButton>(R.id.addingBtn)
        addButton.setOnClickListener {
            val intent = Intent(activity, AddHobbyActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_HOBBY)
        }

        networkHandler = WebSocketClient()
        connectToWebSocketServer()
        sendHobbyListMessage()
        hobbyAdapter?.notifyDataSetChanged()

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_HOBBY && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra("title")
            if (title != null) {
                val numberOfPlayers = data.getIntExtra("numberOfPlayers", 0)
                val hobby = HobbyModel(title, numberOfPlayers)

                sendHobbyMessage(hobby)
                sendHobbyListMessage()
                hobbyAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hobbyAdapter?.notifyDataSetChanged()
    }

    private fun connectToWebSocketServer() {
        networkHandler!!.addMessageHandler(MessageType.HOBBY.toString(), this::messageReceivedFromServer)
        networkHandler!!.addMessageHandler(MessageType.HOBBYLIST.toString(), this::hobbyListFromServer)
        networkHandler!!.connectToServer()
    }

    private fun sendHobbyMessage(hobby: HobbyModel) {
        val hobbyMessage= HobbyMessage()
        hobbyMessage.id = hobby.id
        hobbyMessage.title = hobby.title
        hobbyMessage.number = hobby.number
        val jsonMessage = gson.toJson(hobbyMessage)
        networkHandler?.sendMessageToServer(jsonMessage)
    }

    private fun sendHobbyListMessage() {
        val hobbyListMessage = HobbyListMessage()
        hobbyListMessage.hobbies = null;

        val jsonMessage = gson.toJson(hobbyListMessage)
        networkHandler?.sendMessageToServer(jsonMessage)
    }

    private fun <T> messageReceivedFromServer(message: T) {
        if (message is String) {
            val jsonString = message
        }
    }

    private fun hobbyListFromServer(message: String) {
        val hobbyListMessage = gson.fromJson(message, HobbyListMessage::class.java)
        hobbies.clear()
        hobbies.addAll(hobbyListMessage.hobbies)

        activity?.runOnUiThread {
            hobbyAdapter?.notifyDataSetChanged()
        }
    }

}
