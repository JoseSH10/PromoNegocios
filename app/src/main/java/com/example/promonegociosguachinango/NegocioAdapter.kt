package com.example.promonegociosguachinango

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import com.bumptech.glide.Glide

class NegocioAdapter(
    private val context: Context,
    private var negocios: List<Negocio> // Cambiar a var para poder actualizar la lista
) : RecyclerView.Adapter<NegocioAdapter.NegocioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegocioViewHolder {
        // Asegúrate de que R.layout.item_negocio_horizontal exista y contenga las vistas con los ids correctos
        val view = LayoutInflater.from(context).inflate(R.layout.item_negocio_horizontal, parent, false)
        return NegocioViewHolder(view)
    }

    override fun onBindViewHolder(holder: NegocioViewHolder, position: Int) {
        holder.bind(negocios[position])
    }

    override fun getItemCount(): Int = negocios.size

    // Nuevo método para actualizar los datos del adaptador
    fun actualizarDatos(nuevosNegocios: List<Negocio>) {
        this.negocios = nuevosNegocios
        // Notifica al adaptador que los datos han cambiado.
        // Si estás usando DiffUtil (recomendado para listas grandes y cambios frecuentes),
        // podrías mejorar esto, pero notifyDataSetChanged() es un buen punto de partida.
        notifyDataSetChanged()
    }

    inner class NegocioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre = itemView.findViewById<TextView>(R.id.nombreNegocio)
        private val direccion = itemView.findViewById<TextView>(R.id.direccionNegocio)
        private val imagen = itemView.findViewById<ImageView>(R.id.imagenNegocio)

        fun bind(negocio: Negocio) {
            nombre.text = negocio.nombre
            // Verifica si ubicacion no es nula o si las claves no existen antes de acceder
            val calle = negocio.ubicacion["calle"] ?: ""
            val numero = negocio.ubicacion["numero"] ?: ""
            direccion.text = if (calle.isNotEmpty() || numero.isNotEmpty()) "$calle #$numero" else "Dirección no disponible"

            // Carga la imagen usando Glide
            Glide.with(context)
                .load(negocio.imagen)
                // Asegúrate de que estos drawables existan si no los tienes ya
                .placeholder(R.drawable.placeholder_image) // Opcional: imagen de placeholder
                //.error(R.drawable.error_image) // Opcional: imagen de error
                .into(imagen)
        }
    }
}