package com.example.promonegociosguachinango

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context

class CategoriaAdapter(
    private val context: Context,
    private var categorias: List<Categoria> // Cambiar a var para poder actualizar la lista
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.bind(categorias[position])
    }

    override fun getItemCount(): Int = categorias.size

    // Nuevo método para actualizar los datos del adaptador de categorías
    fun actualizarDatos(nuevasCategorias: List<Categoria>) {
        this.categorias = nuevasCategorias
        notifyDataSetChanged() // Notifica al adaptador de categorías que los datos han cambiado
    }

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloCategoria = itemView.findViewById<TextView>(R.id.textCategoria)
        private val recyclerNegocios = itemView.findViewById<RecyclerView>(R.id.recyclerNegocios)
        private val negocioAdapter: NegocioAdapter // Declara el adaptador de negocios

        init {
            // Configura el RecyclerView de negocios una sola vez
            recyclerNegocios.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            // Inicializa el adaptador de negocios con una lista vacía inicialmente
            negocioAdapter = NegocioAdapter(context, emptyList())
            recyclerNegocios.adapter = negocioAdapter
        }

        fun bind(categoria: Categoria) {
            tituloCategoria.text = categoria.nombre
            // Actualiza los datos del adaptador de negocios interno
            // Pasa la lista de negocios específica para esta categoría
            negocioAdapter.actualizarDatos(categoria.negocios)
        }
    }
}