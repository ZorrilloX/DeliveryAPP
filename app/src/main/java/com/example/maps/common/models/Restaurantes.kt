package com.example.maps.common.models

data class Restaurantes(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Float,
    val longitude: Float,
    val logo: String,
    val products: List<ProductosEnCarrito>
)

data class RestauranteCrear(
    val name: String,
    val address: String,
    val latitude: Float,
    val longitude: Float
)

data class ProductosEnCarrito(
    val id: Long,
    val name: String,
    val description: String,
    val price: String,
    val restaurant_id: Long,
    val image: String? = null
)