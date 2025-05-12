package com.example.promonegociosguachinango

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
//import com.tuapp.nombre.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity() {

    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var tvContrasena: TextView
    private lateinit var ivVerContrasena: ImageView
    private lateinit var btnEditar: ImageButton
    private lateinit var btnAdministrarNegocio: Button
    private lateinit var btnRegistrarNegocio: Button

    private var contrasenaVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Vinculación de vistas
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario)
        tvCorreo = findViewById(R.id.tvCorreo)
        tvContrasena = findViewById(R.id.tvContrasena)
        ivVerContrasena = findViewById(R.id.ivVerContrasena)
        btnEditar = findViewById(R.id.btnEditar)
        btnAdministrarNegocio = findViewById(R.id.btnAdministrarNegocio)
        btnRegistrarNegocio = findViewById(R.id.btnRegistrarNegocio)

        // Simular datos del usuario (después se reemplazará con datos reales desde Firebase)
        tvNombreUsuario.text = "Alma Marcela Silva"
        tvCorreo.text = "almamarcelasilva@gmail.com"
        tvContrasena.text = "********"

        // Mostrar/Ocultar contraseña
        ivVerContrasena.setOnClickListener {
            contrasenaVisible = !contrasenaVisible
            tvContrasena.text = if (contrasenaVisible) "mi_contraseña_segura" else "********"
        }

        // Botón Editar (puedes abrir una nueva actividad para editar el perfil)
        btnEditar.setOnClickListener {
            Toast.makeText(this, "Editar perfil (en desarrollo)", Toast.LENGTH_SHORT).show()
        }

        // Botón Administrar Negocio
        btnAdministrarNegocio.setOnClickListener {
            val intent = Intent(this, AdministrarNegocioActivity::class.java)
            startActivity(intent)
        }

        // Botón Registrar Negocio
        btnRegistrarNegocio.setOnClickListener {
            val intent = Intent(this, RegistrarNegocioActivity::class.java)
            startActivity(intent)
        }
    }
}
