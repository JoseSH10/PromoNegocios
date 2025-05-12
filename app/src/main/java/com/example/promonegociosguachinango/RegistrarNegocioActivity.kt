package com.example.promonegociosguachinango

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID // Para generar nombres únicos para las imágenes
import com.bumptech.glide.Glide // Para cargar la imagen seleccionada en la vista previa
import android.util.Log // Importa la clase Log
import android.view.View // Importa la clase View

class RegistrarNegocioActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etCalle: EditText
    private lateinit var etNumero: EditText
    private lateinit var spinnerCategoria: Spinner
    // Eliminamos etImagen

    private lateinit var btnSeleccionarImagen: Button // Referencia al nuevo botón
    private lateinit var ivImagenPrevia: ImageView // Referencia a la ImageView de vista previa

    private lateinit var btnRegistrar: Button

    private val categorias = mutableListOf<String>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance() // Instancia de Firebase Storage

    private var selectedImageUri: Uri? = null // Para almacenar la URI de la imagen seleccionada

    // Activity Result API para seleccionar imagen de la galería
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null && data.data != null) {
                selectedImageUri = data.data // Guardamos la URI seleccionada
                // Muestra la vista previa de la imagen
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(ivImagenPrevia)
                ivImagenPrevia.visibility = View.VISIBLE // Hace visible la ImageView
            }
        }
    }

    // Activity Result API para solicitar permisos
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, abrir la galería
            abrirGaleria()
        } else {
            // Permiso denegado, mostrar un mensaje al usuario
            Toast.makeText(this, "Permiso de almacenamiento necesario para seleccionar imágenes", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_negocio)

        etNombre = findViewById(R.id.etNombreNegocio)
        etDescripcion = findViewById(R.id.etDescripcion)
        etTelefono = findViewById(R.id.etTelefono)
        etCalle = findViewById(R.id.etCalle)
        etNumero = findViewById(R.id.etNumero)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        // Eliminamos la referencia a etImagen
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen) // Inicializa el nuevo botón
        ivImagenPrevia = findViewById(R.id.ivImagenPrevia) // Inicializa la ImageView de vista previa

        btnRegistrar = findViewById(R.id.btnRegistrarNegocio)

        cargarCategorias()

        btnSeleccionarImagen.setOnClickListener {
            // Lógica para seleccionar imagen (pedir permiso si es necesario)
            verificarPermisoYAbrirGaleria()
        }

        btnRegistrar.setOnClickListener {
            // Ahora el registro incluye la subida de imagen
            registrarNegocio()
        }
    }

    private fun cargarCategorias() {
        db.collection("categorias")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val nombreCategoria = document.getString("nombre")
                    if (nombreCategoria != null) {
                        categorias.add(nombreCategoria)
                    }
                }
                val adapter = ArrayAdapter(this, R.layout.categoria_spinner_item, categorias)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategoria.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar categorías: ${e.message}", Toast.LENGTH_SHORT).show()
                // En caso de error, podrías proporcionar una lista de categorías por defecto:
                categorias.addAll(listOf("Comercio y Ventas",
                    "Comidas y Bebidas",
                    "Servicios Personales",
                    "Servicios Generales",
                    "Transporte",
                    "Tecnología y Comunicación",
                    "Arte, Cultura y Entretenimiento",
                    "Hogar y Decoración",
                    "Educación",
                    "Limpieza y Cuidado del hogar"))
                val adapter = ArrayAdapter(this, R.layout.categoria_spinner_item, categorias)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategoria.adapter = adapter
            }
    }

    private fun verificarPermisoYAbrirGaleria() {
        // El permiso necesario para leer almacenamiento externo puede variar según la versión de Android
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 (API 33) y superior, se necesita READ_MEDIA_IMAGES
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            // Para versiones anteriores, se necesita READ_EXTERNAL_STORAGE
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso ya concedido, abrir la galería directamente
                abrirGaleria()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                // Mostrar una explicación al usuario si el permiso fue denegado previamente
                Toast.makeText(this, "Necesitamos permiso para acceder a tu galería para seleccionar una imagen.", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(permission)
            }
            else -> {
                // Solicitar el permiso
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun registrarNegocio() {
        val nombre = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val calle = etCalle.text.toString().trim()
        val numero = etNumero.text.toString().trim()
        val categoria = spinnerCategoria.selectedItem as? String ?: ""
        // Ya no usamos etImagen.text.toString().trim()
        val propietarioId = auth.currentUser?.uid

        if (nombre.isEmpty() || descripcion.isEmpty() || telefono.isEmpty() ||
            calle.isEmpty() || numero.isEmpty() || categoria.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Por favor, completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show()
            return
        }

        if (propietarioId == null) {
            Toast.makeText(this, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
            return
        }

        // Deshabilita el botón para evitar registros duplicados mientras se sube la imagen
        btnRegistrar.isEnabled = false

        // Subir la imagen a Firebase Storage primero
        uploadImageToFirebaseStorage(selectedImageUri!!) { imageUrl ->
            if (imageUrl != null) {
                // Si la imagen se subió correctamente, registra el negocio en Firestore
                guardarNegocioEnFirestore(nombre, descripcion, telefono, calle, numero, categoria, imageUrl, propietarioId)
            } else {
                // Si hubo un error al subir la imagen
                Toast.makeText(this, "Error al subir la imagen. Inténtalo de nuevo.", Toast.LENGTH_LONG).show()
                btnRegistrar.isEnabled = true // Habilita el botón si falla la subida de imagen
            }
        }
    }

    // Función para subir la imagen a Firebase Storage
    private fun uploadImageToFirebaseStorage(fileUri: Uri, onComplete: (String?) -> Unit) {
        val fileName = UUID.randomUUID().toString() // Genera un nombre único para la imagen
        val storageRef = storage.reference.child("imagenes_negocios/$fileName")

        storageRef.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                // La imagen se subió con éxito, ahora obtenemos la URL de descarga
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    Log.d("RegistrarNegocio", "Imagen subida exitosamente. URL: $imageUrl")
                    onComplete(imageUrl) // Llamamos al callback con la URL
                }.addOnFailureListener { e ->
                    Log.e("RegistrarNegocio", "Error al obtener la URL de descarga: ${e.message}", e)
                    Toast.makeText(this, "Error al obtener la URL de la imagen.", Toast.LENGTH_SHORT).show()
                    onComplete(null) // Llamamos al callback con null en caso de error
                }
            }
            .addOnFailureListener { e ->
                Log.e("RegistrarNegocio", "Error al subir la imagen a Storage: ${e.message}", e)
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                onComplete(null) // Llamamos al callback con null en caso de error
            }
    }

    // Función para guardar los datos del negocio en Firestore
    private fun guardarNegocioEnFirestore(
        nombre: String,
        descripcion: String,
        telefono: String,
        calle: String,
        numero: String,
        categoria: String,
        imageUrl: String,
        propietarioId: String
    ) {
        val nuevoNegocioRef = db.collection("negocios").document()
        val negocioID = nuevoNegocioRef.id

        val negocio = hashMapOf(
            "negocioID" to negocioID,
            "nombre" to nombre,
            "descripcion" to descripcion,
            "telefono" to telefono,
            "categoria" to categoria,
            "imagen" to imageUrl, // Usamos la URL de la imagen subida
            "ubicacion" to mapOf(
                "calle" to calle,
                "numero" to numero
            ),
            "estado" to "pendiente",
            "propietarioId" to propietarioId
        )

        nuevoNegocioRef.set(negocio)
            .addOnSuccessListener {
                Toast.makeText(this, "Negocio enviado para aprobación", Toast.LENGTH_LONG).show()
                btnRegistrar.isEnabled = true // Habilita el botón al finalizar
                finish() // Cierra la actividad después de un registro exitoso
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al registrar el negocio: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("RegistrarNegocio", "Error al guardar en Firestore: ${e.message}", e)
                btnRegistrar.isEnabled = true // Habilita el botón si falla
            }
    }
}
