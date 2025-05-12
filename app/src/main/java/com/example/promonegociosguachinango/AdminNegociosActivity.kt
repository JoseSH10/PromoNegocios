package com.example.promonegociosguachinango

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminNegociosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NegocioPendienteAdapter
    private val db = FirebaseFirestore.getInstance()
    private val negociosPendientes = mutableListOf<Negocio>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_negocios_activity)

        recyclerView = findViewById(R.id.recyclerNegocios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NegocioPendienteAdapter(negociosPendientes,
            onAprobar = { negocio -> cambiarEstado(negocio.negocioID, "aprobado") },
            onRechazar = { negocio -> cambiarEstado(negocio.negocioID, "rechazado") }
        )
        recyclerView.adapter = adapter

        cargarNegociosPendientes()
    }

    private fun cargarNegociosPendientes() {
        db.collection("negocios")
            .whereEqualTo("estado", "pendiente")
            .get()
            .addOnSuccessListener { documents ->
                negociosPendientes.clear()
                for (doc in documents) {
                    val negocio = doc.toObject(Negocio::class.java)
                    negociosPendientes.add(negocio)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar negocios", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cambiarEstado(negocioId: String, nuevoEstado: String) {
        db.collection("negocios").document(negocioId)
            .update("estado", nuevoEstado)
            .addOnSuccessListener {
                Toast.makeText(this, "Negocio $nuevoEstado", Toast.LENGTH_SHORT).show()
                cargarNegociosPendientes() // Recarga la lista
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar estado", Toast.LENGTH_SHORT).show()
            }
    }
}
