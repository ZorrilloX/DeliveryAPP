package com.example.maps.common.repositories

import android.util.Log
import com.example.maps.common.api.DeliveryApiService
import com.example.maps.common.models.Restaurantes
import com.example.maps.common.models.RestauranteCrear
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context

class RestaurantRepository(context: Context) {

    private val api = RetrofitRepository(context).getRetrofitInstance().create(DeliveryApiService::class.java)

    // Obtener lista de restaurantes
    fun fetchRestaurantes(
        token: String,
        onSuccess: (List<Restaurantes>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.getRestaurantes("Bearer $token").enqueue(object : Callback<List<Restaurantes>> {
            override fun onResponse(call: Call<List<Restaurantes>>, response: Response<List<Restaurantes>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("RestauranteRepository", "Error al obtener restaurantes: $errorBody")
                    onError(Throwable("Error al obtener restaurantes: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Restaurantes>>, t: Throwable) {
                Log.e("RestauranteRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }

    // Obtener detalle de un restaurante
    fun fetchRestauranteDetalle(
        token: String,
        id: Long,
        onSuccess: (Restaurantes) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.getRestauranteDetalle("Bearer $token", id).enqueue(object : Callback<Restaurantes> {
            override fun onResponse(call: Call<Restaurantes>, response: Response<Restaurantes>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("RestauranteRepository", "Productos recibidos: ${it.products}")
                        onSuccess(it)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("RestauranteRepository", "Error al obtener detalle de restaurante: $errorBody")
                    onError(Throwable("Error al obtener detalle: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Restaurantes>, t: Throwable) {
                Log.e("RestauranteRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }

    // Crear un restaurante
    fun createRestaurante(
        token: String,
        restauranteCrear: RestauranteCrear,
        onSuccess: (Restaurantes) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.createRestaurante("Bearer $token", restauranteCrear).enqueue(object : Callback<Restaurantes> {
            override fun onResponse(call: Call<Restaurantes>, response: Response<Restaurantes>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("RestauranteRepository", "Error al crear restaurante: $errorBody")
                    onError(Throwable("Error al crear restaurante: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Restaurantes>, t: Throwable) {
                Log.e("RestauranteRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }

    // Aquí puedes añadir más funciones como actualizar o eliminar si es necesario, por ahora nos enfocamos en las más comunes.
}
