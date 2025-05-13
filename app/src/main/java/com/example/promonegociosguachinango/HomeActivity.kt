package com.example.promonegociosguachinango

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log // Importa la clase Log
import android.view.View
import android.widget.FrameLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerViewCategorias: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter
    private val listaCategorias = mutableListOf<Categoria>()
    private val db = Firebase.firestore
    private var categoriasCargadasCount = 0 // Contador para saber cuántas categorías se han procesadas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias)
        recyclerViewCategorias.layoutManager = LinearLayoutManager(this)

        // Inicializa el adaptador con la lista mutable
        categoriaAdapter = CategoriaAdapter(this, listaCategorias)
        recyclerViewCategorias.adapter = categoriaAdapter

        obtenerCategoriasConNegocios()

        // Bottom navigation opcional
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategorias)
            val contenedorFragmentos = findViewById<FrameLayout>(R.id.contenedorFragmentos)
            when (it.itemId) {
                R.id.nav_home -> {
                    recyclerView.visibility = View.VISIBLE
                    contenedorFragmentos.visibility = View.GONE
                    supportFragmentManager.popBackStack() // Regresa si hay fragmento
                    true
                }
                R.id.nav_search -> {
                    recyclerView.visibility = View.GONE
                    contenedorFragmentos.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragmentos, BuscarFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.nav_favorites -> {
                    // Lógica para Favoritos
                    true
                }
                R.id.nav_map -> {
                    // Lógica para Mapa
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    // Opcional: finish() si no quieres volver a Home con el botón atrás
                    true
                }
                else -> false
            }
        }
    }

    private fun obtenerCategoriasConNegocios() {
        val categorias = listOf(
            "Comercio y Ventas",
            "Comidas y Bebidas",
            "Servicios Personales",
            "Servicios Generales",
            "Transporte",
            "Tecnología y Comunicación",
            "Arte, Cultura y Entretenimiento",
            "Hogar y Decoración",
            "Educación",
            "Limpieza y Cuidado del hogar"
        )

        val totalCategorias = categorias.size
        listaCategorias.clear() // Limpia la lista antes de agregar nuevas categorías
        categoriasCargadasCount = 0 // Reinicia el contador

        for (categoria in categorias) {
            db.collection("negocios")
                .whereEqualTo("categoria", categoria) // Ahora busca el campo 'categoria'
                .whereEqualTo("estado", "aprobado")
                .get()
                .addOnSuccessListener { result ->
                    // Log para ver cuántos documentos se encontraron para esta categoría
                    Log.d("FirestoreDebug", "Categoría: $categoria, Documentos encontrados: ${result.documents.size}")

                    val negocios = result.documents.mapNotNull { it.toObject(Negocio::class.java) }
                    // Log para ver cuántos objetos Negocio válidos se crearon
                    Log.d("FirestoreDebug", "Categoría: $categoria, Objetos Negocio válidos creados: ${negocios.size}")

                    if (negocios.isNotEmpty()) {
                        // Agregar a la lista mutable solo si hay negocios
                        listaCategorias.add(Categoria(categoria, negocios))
                        // Log para confirmar que se agregó la categoría
                        Log.d("FirestoreDebug", "Categoría agregada: ${categoria}")
                    } else {
                        // Log si no se encontraron negocios aprobados para una categoría
                        Log.d("FirestoreDebug", "No se encontraron negocios aprobados para la categoría: $categoria")
                    }

                    categoriasCargadasCount++ // Incrementa el contador después de procesar cada categoría

                    // Si todas las consultas han terminado, actualiza el adaptador principal
                    if (categoriasCargadasCount == totalCategorias) {
                        // Opcional: ordena listaCategorias si el orden importa
                        // Asegúrate de que tu clase Categoria tenga una propiedad 'nombre' para poder ordenar
                        listaCategorias.sortBy { categorias.indexOf(it.nombre) } // Asumiendo que Categoria tiene 'nombre'

                        // Log antes de actualizar el adaptador
                        Log.d("FirestoreDebug", "Todas las consultas terminadas. Total de categorías con negocios: ${listaCategorias.size}. Actualizando adaptador principal.")

                        // Actualiza los datos del adaptador principal
                        categoriaAdapter.actualizarDatos(listaCategorias)
                    }
                }
                .addOnFailureListener { exception ->
                    // Log para registrar errores en la consulta de Firestore
                    Log.e("FirestoreError", "Error obteniendo negocios para categoría $categoria", exception)
                    categoriasCargadasCount++ // Asegura que el contador se incremente incluso en caso de error
                    // Si todas las consultas han terminado (incluso con errores), aún así intentamos actualizar
                    if (categoriasCargadasCount == totalCategorias) {
                        Log.e("FirestoreError", "Todas las consultas terminadas. Hubo errores en alguna(s). Intentando actualizar adaptador principal.")
                        // Aunque hubo errores, actualizamos con las categorías que sí se cargaron
                        categoriaAdapter.actualizarDatos(listaCategorias)
                    }
                }
        }
    }
}