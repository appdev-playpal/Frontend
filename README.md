Frontend Client - PlayPal
==============

Overview
--------

This is an Android-based frontend client written in Kotlin and Java using Android Studio. The client provides a user-friendly interface for managing hobbies, locations, profiles, and settings.

Features
--------

### Login Activity

* Allows users to sign up with their email
* Authenticates users and redirects them to the Main Activity

### Main Activity

* Features a toolbar with four navigation options:
	+ Hobby
	+ Location
	+ Profile
	+ Settings
* Each navigation option corresponds to a separate fragment

### Hobby Fragment

* Allows users to add new hobbies
* Displays a list of all hobbies fetched from the server
* When a user adds a new hobby, it is sent to the server for storage

### Location Fragment

* Displays the user's current location

### Profile Fragment

* Allows users to enter personal information
* Provides a logout option, which redirects the user to the Login Activity

### Settings Fragment

* Provides settings options for the app

Database
---------

* A database is included in the project, but it is not currently used
* The plan is to use the database to store hobbies locally in case of server issues

Technology Stack
----------------

* **Programming Languages:** Kotlin, Java
* **Development Environment:** Android Studio
* **Database:** SQLite

Development
-----------

To develop and test the frontend client, follow these steps:

1. Clone the repository
2. Open the project in Android Studio
3. Start the Server (Backend)
4. Build and run the app on an emulator or physical device
5. Test the app's features and functionality

Contributing
------------

Contributions to the frontend client are welcome! If you'd like to contribute, please follow these steps:

1. Fork the repository
2. Make your changes
3. Submit a pull request
