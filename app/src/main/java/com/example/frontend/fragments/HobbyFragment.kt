package com.example.frontend.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.activities.AddHobbyActivity
import com.example.frontend.adapters.HobbyAdapter
import com.example.frontend.models.HobbyModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HobbyFragment : Fragment() {

    private lateinit var hobbyAdapter: HobbyAdapter
    private val hobbies = mutableListOf<HobbyModel>()

    companion object {
        const val REQUEST_CODE_ADD_HOBBY = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hobby, container, false)

        // RecyclerView setup
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler)
        hobbyAdapter = HobbyAdapter(hobbies)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = hobbyAdapter

        // FloatingActionButton to add a new hobby
        view.findViewById<FloatingActionButton>(R.id.addingBtn).setOnClickListener {
            val intent = Intent(activity, AddHobbyActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_HOBBY)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_HOBBY && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra("title") ?: return
            val numberOfPlayers = data.getIntExtra("numberOfPlayers", 0)
            val hobby = HobbyModel(title, numberOfPlayers)
            hobbies.add(hobby)
            hobbyAdapter.notifyDataSetChanged()
        }
    }
}
