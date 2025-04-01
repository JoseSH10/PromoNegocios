package com.example.promonegociosguachinango

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Inicializar Firebase ANTES de cualquier otra operación
        FirebaseApp.initializeApp(this)
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

        //  Llamar funciones en el orden correcto
        agregarNegocio(this)
        escucharCambiosNegocios()
    }
}
// Función para agregar un negocio
fun agregarNegocio(context: Context) {
    val db = FirebaseFirestore.getInstance()

    val negocios = hashMapOf(
        "categoriaId" to "ID_CATEGORIA",
        "coordenadas" to GeoPoint(20.666, -103.350),
        "descripcion" to "Descripción del negocio",
        "estado" to "Activo",
        "imagen" to "https://ejemplo.com/imagen.jpg",
        "negocioID" to "ID_NEGOCIO",
        "nombre" to "cafeli",
        "propietarioId" to "ID_PROPIETARIO",
        "telefono" to "1234567468",
        /*"ubicacion" to hashMapOf(
            "calle" to "Av. Principal",
            "numero" to "123",
            "colonia" to "Centro",
            "ciudad" to "Guadalajara",
            "codigoPostal" to "44100"
        ),*/
        "valoraciones" to listOf(5, 4, 3)
    )

    Log.d("Firestore", "Intentando agregar negocio...") // Mensaje de depuración

    db.collection("negocios").document("ID_NEGOCIO")
        .set(negocios)
        .addOnSuccessListener {
            Log.d("Firestore", "Negocio agregado con éxito") // Verifica si aparece este log en Logcat
            Toast.makeText(context, "Negocio agregado correctamente", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al agregar negocio", e) //  Si hay un error, aparecerá aquí
        }
}


// Función para escuchar cambios en la colección "negocios"
fun escucharCambiosNegocios() {
    val db = FirebaseFirestore.getInstance()

    db.collection("negocios").addSnapshotListener { snapshots, e ->
        if (e != null) {
            Log.e("Firestore", "Error al escuchar cambios", e)
            return@addSnapshotListener
        }

        for (doc in snapshots!!.documentChanges) {
            when (doc.type) {
                DocumentChange.Type.ADDED -> Log.d("Firestore", "Nuevo negocio: ${doc.document.data}")
                DocumentChange.Type.MODIFIED -> Log.d("Firestore", "Negocio actualizado: ${doc.document.data}")
                DocumentChange.Type.REMOVED -> Log.d("Firestore", "Negocio eliminado: ${doc.document.data}")
            }
        }
    }
}

// Función para actualizar un negocio
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

// Función para eliminar un negocio
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

// Función para obtener negocios manualmente
fun obtenerNegocios() {
    val db = FirebaseFirestore.getInstance()

    db.collection("negocios").get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d("Firestore", "ID: ${document.id}, Datos: ${document.data}")
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al obtener negocios", e)
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
