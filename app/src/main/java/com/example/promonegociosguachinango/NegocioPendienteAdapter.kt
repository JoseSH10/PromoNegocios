package com.example.promonegociosguachinango

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NegocioPendienteAdapter(
    private val negocios: List<Negocio>,
    private val onAprobar: (Negocio) -> Unit,
    private val onRechazar: (Negocio) -> Unit
) : RecyclerView.Adapter<NegocioPendienteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val btnAprobar: Button = view.findViewById(R.id.btnAprobar)
        val btnRechazar: Button = view.findViewById(R.id.btnRechazar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_negocio_pendiente, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = negocios.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val negocio = negocios[position]
        holder.tvNombre.text = negocio.nombre
        holder.tvDescripcion.text = negocio.descripcion

        holder.btnAprobar.setOnClickListener { onAprobar(negocio) }
        holder.btnRechazar.setOnClickListener { onRechazar(negocio) }
    }
}