package com.example.promonegociosguachinango

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class BuscarFragment: Fragment() {

    private lateinit var editTextBusqueda: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NegocioAdapter
    private var listaNegocios = mutableListOf<Negocio>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buscar, container, false)

        editTextBusqueda = view.findViewById(R.id.editTextBusqueda)
        recyclerView = view.findViewById(R.id.recyclerViewResultados)

        adapter = NegocioAdapter(requireContext(), listaNegocios)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        cargarNegociosDesdeFirebase()

        editTextBusqueda.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filtrarNegocios(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return view
    }

    private fun cargarNegociosDesdeFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("negocios")
            .get()
            .addOnSuccessListener { documents ->
                listaNegocios.clear()
                for (doc in documents) {
                    val negocio = doc.toObject(Negocio::class.java)
                    listaNegocios.add(negocio)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun filtrarNegocios(texto: String) {
        val filtrados = listaNegocios.filter {
            it.nombre.contains(texto, ignoreCase = true)
        }
        adapter.actualizarLista(filtrados)
    }
}
