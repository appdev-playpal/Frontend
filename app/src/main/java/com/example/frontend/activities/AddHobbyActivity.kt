package com.example.frontend.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.example.frontend.database.Database_Helper
import com.example.frontend.models.HobbyModel

class AddHobbyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_hobby)

        val hobbyHint: EditText = findViewById(R.id.hobbyHint)
        val requiredPlayers: Spinner = findViewById(R.id.requiredPlayers)
        val addHobbyButton: Button = findViewById(R.id.add_hobby_button)

        ArrayAdapter.createFromResource(
            this,
            R.array.players_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            requiredPlayers.adapter = adapter
        }

        addHobbyButton.setOnClickListener {
            val title = hobbyHint.text.toString()
            val numberOfPlayers = requiredPlayers.selectedItem.toString().toInt()

            // Add hobby to the database
            val hobby = HobbyModel(title, numberOfPlayers)
            val databaseHelper = Database_Helper(this)
            val success = databaseHelper.addHobbySmall(hobby)
            if (success) {
                Log.d("Database", "Hobby added successfully")
            } else {
                Log.d("Database", "Failed to add hobby")
            }

            val resultIntent = Intent()
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("numberOfPlayers", numberOfPlayers)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
