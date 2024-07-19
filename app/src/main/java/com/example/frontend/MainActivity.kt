package com.example.frontend

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frontend.database.Database_Helper
import com.example.frontend.models.HobbyModel

class MainActivity : AppCompatActivity() {

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
    }
    /*
    private fun addHobby(hobby: HobbyModel){
        dbHandler!!.addHobby(hobby)
    }
    */
}