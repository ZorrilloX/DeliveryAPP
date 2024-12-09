package com.example.maps.common.models
data class PedidoCompleto( //MODELO PARA RECIBIR LA LISTA DE PEDIDOS CON GET ORDER
    val id: Long,
    val user_id: Long,
    val restaurant_id: Long,
    val total: String,
    val latitude: String,
    val longitude: String,
    val address: String,
    val driver_id: Long?,
    val status: String,
    val created_at: String,
    val delivery_proof: String,
    val order_details: List<OrderDetail>
)
data class OrderDetail(
    val id: Long,
    val quantity: Int,
    val price: String,
    val product: Product
)
data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Float,
    val image: String
)



//MODELO PARA HACER EL POST ----------------------------------------------------------------
data class PedidoCrear( //para el POST
    val restaurant_id: Long,
    val total: Float,
    val address: String,
    val latitude: String,
    val longitude: String,
    val details: List<PedidoItemCrear>
)
data class PedidoItemCrear( //para el POST
    val product_id: Long,
    val qty: Long,
    val price: String
)


//MODELO PARA IMPRIMIR EN PANTALLA -------------------------------------------------
data class PedidoItem( //PARA IMPRIMIR
    val id: Long,
    val name: String,
    val price: Float,
    val quantity: Int,
    val image: String
)

