package com.example.maps.common.repositories


import android.content.Context
import android.util.Log
import com.example.maps.common.api.DeliveryApiService
import com.example.maps.common.models.PedidoCompleto
import com.example.maps.common.models.PedidoCrear
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoRepository(context: Context) {

    private val api = RetrofitRepository(context).getRetrofitInstance().create(DeliveryApiService::class.java)

    // Crear un pedido
    fun createPedido(
        token: String,
        pedidoCrear: PedidoCrear,
        onSuccess: () -> Unit, // Cambiado para no esperar datos
        onError: (Throwable) -> Unit
    ) {
        api.createPedido("Bearer $token", pedidoCrear).enqueue(object : Callback<Void> { // Cambiado a Void si no necesitas datos de la respuesta
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PedidoRepository", "Error al crear pedido: $errorBody")
                    onError(Throwable("Error al crear pedido: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PedidoRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }
    fun getMisPedidos(token: String, onSuccess: (List<PedidoCompleto>) -> Unit, onError: (Throwable) -> Unit) {
        api.getMisPedidos("Bearer $token").enqueue(object : Callback<List<PedidoCompleto>> {
            override fun onResponse(call: Call<List<PedidoCompleto>>, response: Response<List<PedidoCompleto>>) {
                if (response.isSuccessful) {
                    val pedidosCompletos = response.body() ?: emptyList()
                    onSuccess(pedidosCompletos)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PedidoRepository", "Error al obtener pedidos: $errorBody")
                    onError(Throwable("Error al obtener pedidos: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<PedidoCompleto>>, t: Throwable) {
                Log.e("PedidoRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }


    //seccion para el chofer ----------------------------------------------------------------

    fun getPedidosLibres(token: String, onSuccess: (List<PedidoCompleto>) -> Unit, onError: (Throwable) -> Unit) {
        api.getPedidosLibres("Bearer $token").enqueue(object : Callback<List<PedidoCompleto>> {
            override fun onResponse(call: Call<List<PedidoCompleto>>, response: Response<List<PedidoCompleto>>) {
                if (response.isSuccessful) {
                    val pedidosCompletos = response.body() ?: emptyList()
                    onSuccess(pedidosCompletos)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PedidoRepository", "Error al obtener pedidos Libres: $errorBody")
                    onError(Throwable("Error al obtener pedidos: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<PedidoCompleto>>, t: Throwable) {
                Log.e("PedidoRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }

    fun aceptarPedido(token: String, idPedido: Long, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        api.aceptarPedido("Bearer $token", idPedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("PedidoRepository", "Pedido aceptado por el chofer correctamente")
                    onSuccess()  // Llama al onSuccess cuando el pedido sea aceptado correctamente.
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PedidoRepository", "Error al aceptar pedido: $errorBody")
                    onError(Throwable("Error al aceptar pedido: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PedidoRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }

}