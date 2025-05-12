package com.example.promonegociosguachinango

data class Negocio(
    val negocioID: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val estado: String = "",
    val ubicacion: Map<String, String> = emptyMap(),
    val imagen: String = ""
)