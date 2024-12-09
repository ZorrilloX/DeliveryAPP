package com.example.maps.common.api

import com.example.maps.common.models.LoginRequest
import com.example.maps.common.models.LoginResponse
import com.example.maps.common.models.PedidoCompleto
import com.example.maps.common.models.PedidoCrear
import com.example.maps.common.models.RestauranteCrear
import com.example.maps.common.models.Restaurantes
import com.example.maps.common.models.Usuario
import com.example.maps.common.models.UsuarioRegistar
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface DeliveryApiService {
    @POST("users")
    fun registerUser(@Body usuarioData: UsuarioRegistar ): Call<Usuario>

    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("me")
    fun getUserData(@Header("Authorization") token: String): Call<Usuario>



    @GET("restaurants")
    fun getRestaurantes(@Header("Authorization") token: String): Call<List<Restaurantes>>

    // Obtener detalle de un restaurante
    @GET("restaurants/{id}")
    fun getRestauranteDetalle(@Header("Authorization") token: String, @Path("id") id: Long): Call<Restaurantes>

    // Crear restaurante
    @POST("restaurants")
    fun createRestaurante(@Header("Authorization") token: String, @Body restauranteCrear: RestauranteCrear): Call<Restaurantes>



    //crear pedido
    @POST("orders")
    fun createPedido(@Header("Authorization") token: String, @Body orderRequest: PedidoCrear): Call<Void>

    @GET("orders")
    fun getMisPedidos(@Header("Authorization") token: String): Call<List<PedidoCompleto>>

    //chofer pedidos
    @GET("orders/free")
    fun getPedidosLibres(@Header("Authorization") token: String): Call<List<PedidoCompleto>>

    @POST("orders/{id}/accept")
    fun aceptarPedido(@Header("Authorization") token: String, @Path("id") id: Long): Call<Void>


}