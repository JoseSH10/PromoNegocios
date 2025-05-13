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
    private var negocios: List<Negocio>
) : RecyclerView.Adapter<NegocioAdapter.NegocioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegocioViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_negocio, parent, false)
        return NegocioViewHolder(view)
    }

    override fun onBindViewHolder(holder: NegocioViewHolder, position: Int) {
        holder.bind(negocios[position])
    }

    override fun getItemCount(): Int = negocios.size

    fun actualizarLista(nuevosNegocios: List<Negocio>) {
        this.negocios = nuevosNegocios
        notifyDataSetChanged()
    }

    inner class NegocioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre = itemView.findViewById<TextView>(R.id.txtNombreNegocio)
        private val descripcion = itemView.findViewById<TextView>(R.id.txtDescripcionNegocio)
        private val imagen = itemView.findViewById<ImageView>(R.id.imgNegocio)

        fun bind(negocio: Negocio) {
            nombre.text = negocio.nombre
            descripcion.text = negocio.descripcion

            // Glide para cargar la imagen
            Glide.with(context)
                .load(negocio.imagen) // asegúrate de que `imagen` sea la URL
                .placeholder(R.drawable.placeholder_image)
                //.error(R.drawable.error_image)
                .into(imagen)
        }
    }
}
