package com.example.proyectorestaurante

data class ProductoUI(
    val id: String = "",
    val nombre: String,
    val precio: Double,
    val imagenUrl: String? = null,
    val imagenRes: Int? = null,
    var cantidad: Int = 1
)