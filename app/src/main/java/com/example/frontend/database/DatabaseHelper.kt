package com.example.frontend.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.frontend.adapters.HobbyAdapter
import com.example.frontend.models.HobbyModel

class Database_Helper(context: Context) : SQLiteOpenHelper(context, _dbName, null, _dbVersion) {

    private var idCounter = 1

    companion object {
        private val _dbName = "playpal"
        private val _dbVersion = 1
        private val tableName = "hobbies"
        private val id = "id"
        private val user = "user"
        private val title = "title"
        private val description = "description"
        private val number = "number"
        private val date = "date"
        private val location = "location"
        private val latitude = "latitude"
        private val longitude = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val Create_Table = "CREATE TABLE $tableName ($id INTEGER PRIMARY KEY AUTOINCREMENT, $user TEXT, $title TEXT, $description TEXT, $number INTEGER, $date TEXT, $location TEXT, $latitude DOUBLE, $longitude DOUBLE);"
        db?.execSQL(Create_Table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val Drop_Table = "DROP TABLE IF EXISTS $tableName"
        db?.execSQL(Drop_Table)
        onCreate(db)
    }

    /// <summary>
    /// Gets all hobbies from the database
    /// </summary>
    /// <param></param>
    /// <returns>
    /// A list of hobbies
    /// </returns>
    @SuppressLint("Range")
    fun getAllHobbies(): List<HobbyModel> {
        val hobbyList = ArrayList<HobbyModel>()

        val db = writableDatabase
        val selectQuery = "SELECT *FROM $tableName"

        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val hobby = HobbyModel()
                    hobby.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(id)))
                    hobby.user = cursor.getString(cursor.getColumnIndex(user))
                    hobby.title = cursor.getString(cursor.getColumnIndex(title))
                    hobby.description = cursor.getString(cursor.getColumnIndex(description))
                    hobby.number = cursor.getInt(cursor.getColumnIndex(number))
                    hobby.date = cursor.getString(cursor.getColumnIndex(date))
                    hobby.location = cursor.getString(cursor.getColumnIndex(location))
                    hobby.latitude = cursor.getDouble(cursor.getColumnIndex(latitude))
                    hobby.longitude = cursor.getDouble(cursor.getColumnIndex(longitude))
                    hobbyList.add(hobby)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return hobbyList
    }

    /// <summary>
    /// Adds a hobby to the database
    /// </summary>
    /// <param name="NewHobby"><c>HobbyModel</c> A new instance of type HobbyModel</param>
    /// <returns>
    /// A boolean to see if the addition was successful
    /// </returns>
    fun addHobby(NewHobby : HobbyModel): Boolean{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(id, NewHobby.id)
        values.put(user, NewHobby.user)
        values.put(title, NewHobby.title)
        values.put(description, NewHobby.description)
        values.put(number, NewHobby.number)
        values.put(date, NewHobby.date)
        values.put(location, NewHobby.location)
        values.put(latitude, NewHobby.latitude)
        values.put(longitude, NewHobby.longitude)

        val _success = db.insert(tableName, null, values)
        db.close()

        return (Integer.parseInt("$_success") != -1)
    }

    /// <summary>
    /// Adds a hobby with reduced properties to the database
    /// </summary>
    /// <param name="NewHobby"><c>HobbyModel</c> A new instance of type HobbyModel</param>
    /// <returns>
    /// A boolean to see if the addition was successful
    /// </returns>
    fun addHobbySmall(NewHobby : HobbyModel): Boolean {
        val db = this.writableDatabase

        // Get the maximum ID
        val cursor = db.query(tableName, arrayOf("MAX($id)"), null, null, null, null, null)
        var maxId = 0
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(0)
        }
        cursor.close()

        // Generate a new ID
        NewHobby.id = maxId + 1

        val values = ContentValues()
        values.put(id, NewHobby.id)
        values.put(title, NewHobby.title)
        values.put(number, NewHobby.number)

        val _success = db.insert(tableName, null, values)
        db.close()

        if (_success!= -1L) {
            // Notify the adapter of the change
            return true
        } else {
            return false
        }
    }

    /// <summary>
    /// Deletes a hobby from the database
    /// </summary>
    /// <param name="id"><c>Int</c> The ID of the hobby to be deleted</param>
    /// <returns>
    /// A boolean to see if the deletion was successful
    /// </returns>
    fun removeHobby(id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(tableName, "$id =?", arrayOf(id.toString()))
        db.close()
        return (_success != 0)
    }
}