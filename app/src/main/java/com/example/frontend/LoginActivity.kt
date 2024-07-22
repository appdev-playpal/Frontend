package com.example.frontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {
    // Deklariere Variablen für die UI-Elemente und den GoogleSignInClient
    private lateinit var emailInput: EditText
    private lateinit var loginButton: Button
    private lateinit var googleButton: ImageView

    private lateinit var googleSignInClient: GoogleSignInClient

    // Konstanten für den Sign-In-Request-Code
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Finde die UI-Elemente
        emailInput = findViewById(R.id.email_input)
        loginButton = findViewById(R.id.login_button)
        googleButton = findViewById(R.id.google_button)

        // Konfiguriere Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Setze den OnClickListener für den Google-Button
        googleButton.setOnClickListener {
            signIn()
        }

        // Setze den OnClickListener für den Login-Button
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            if (email.isNotEmpty()) {
                navigateToHome()
            } else {
                // Zeige eine Fehlermeldung an oder handle leeres E-Mail-Feld
                emailInput.error = "Bitte E-Mail eingeben"
            }
        }
    }

    // Funktion, um die Google Sign-In Aktivität zu starten
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Ergebnis des Sign-In-Versuchs verarbeiten
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    handleSignInResult(account)
                }
            } catch (e: ApiException) {
                // Handle failure
                e.printStackTrace()
            }
        }
    }

    // Nach erfolgreicher Anmeldung zur HomeActivity navigieren
    private fun handleSignInResult(account: GoogleSignInAccount) {
        navigateToHome()
    }

    // Funktion, um zur HomeActivity zu navigieren
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
