package com.example.promonegociosguachinango

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val texto = findViewById<TextView>(R.id.textAdmin)
        texto.text = "Bienvenido, administrador 👑"
    }
}
