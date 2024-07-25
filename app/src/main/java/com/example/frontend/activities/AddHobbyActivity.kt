package com.example.frontend.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R

class AddHobbyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_hobby)

        val requiredPlayers: Spinner = findViewById(R.id.requiredPlayers)

        // Set up the spinner with the players array
        ArrayAdapter.createFromResource(
            this,
            R.array.players_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            requiredPlayers.adapter = adapter
        }
    }
}
