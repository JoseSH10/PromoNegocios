package com.example.promonegociosguachinango

data class Categoria(
    val nombre: String = "",
    val negocios: List<Negocio> = listOf()
)
