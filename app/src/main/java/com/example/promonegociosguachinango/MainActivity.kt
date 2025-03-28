package com.example.promonegociosguachinango

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.promonegociosguachinango.ui.theme.PromonegociosGuachinangoTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agregarNegocio()
        obtenerNegocios()
        actualizarNegocio("ID_DEL_NEGOCIO", "Nuevo Nombre")
        eliminarNegocio("ID_DEL_NEGOCIO")

        FirebaseApp.initializeApp(this) // Initialize Firebase
        val db = FirebaseFirestore.getInstance()
        enableEdgeToEdge()
        setContent {
            PromonegociosGuachinangoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
fun agregarNegocio() {
    val db = FirebaseFirestore.getInstance()

    val negocio = hashMapOf(
        "nombre" to "Tienda Ejemplo",
        "descripcion" to "Venta de productos electrónicos",
        //"direccion" to "Calle 123, Guachinango",
        "telefono" to "3312345678",
        "categoriaId" to "restaurantes",
        "coordenadas" to hashMapOf(
            "latitud" to 20.758,
            "longitud" to -104.243
        )
    )

    db.collection("negocios")
        .add(negocio)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "Negocio agregado con ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al agregar negocio", e)
        }
}
fun obtenerNegocios() {
    val db = FirebaseFirestore.getInstance()

    db.collection("negocios")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d("Firestore", "ID: ${document.id}, Datos: ${document.data}")
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al obtener negocios", e)
        }
}
fun actualizarNegocio(negocioID: String, nuevoNombre: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("negocios").document(negocioID)
        .update("nombre", nuevoNombre)
        .addOnSuccessListener {
            Log.d("Firestore", "Negocio actualizado")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al actualizar", e)
        }
}
fun eliminarNegocio(negocioID: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("negocios").document(negocioID)
        .delete()
        .addOnSuccessListener {
            Log.d("Firestore", "Negocio eliminado")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al eliminar", e)
        }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PromonegociosGuachinangoTheme {
        Greeting("Android")
    }
}