package com.example.frontend.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.frontend.R
import com.example.frontend.activities.LoginActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Find the buttonLogout by its ID
        val buttonLogout: Button = view.findViewById(R.id.buttonLogout)

        buttonLogout.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            // Optionally, finish the current activity if the fragment is part of it
            activity?.finish()
        }

        return view
    }
}

