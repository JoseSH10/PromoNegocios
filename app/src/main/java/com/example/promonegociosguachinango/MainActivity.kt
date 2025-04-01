package com.example.promonegociosguachinango

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Si el usuario ya está logueado, lo mandamos al Home
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            // Si no está logueado, lo mandamos al Login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}