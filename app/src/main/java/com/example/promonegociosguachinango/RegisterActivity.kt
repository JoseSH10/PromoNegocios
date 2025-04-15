package com.example.promonegociosguachinango

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailField = findViewById<EditText>(R.id.registerEmail)
        val passwordField = findViewById<EditText>(R.id.registerPassword)
        val nameField = findViewById<EditText>(R.id.registerName)
        val phoneField = findViewById<EditText>(R.id.registerPhone)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val name = nameField.text.toString()
            val phone = phoneField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: ""
                            val user = hashMapOf(
                                "usuarioId" to userId,
                                "email" to email,
                                "nombre" to name,
                                "telefono" to phone,
                                "rol" to "usuario",  // El rol por defecto
                                "fechaRegistro" to Date()
                            )
                            db.collection("usuarios").document(userId).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error al guardar en Firestore", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


