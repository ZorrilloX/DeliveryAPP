package com.example.maps.restaurante.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.maps.common.models.PedidoCompleto
import com.example.maps.common.models.PedidoCrear
import com.example.maps.common.models.PedidoItemCrear
//import com.example.maps.common.models.Pedidos
import com.example.maps.common.models.ProductosEnCarrito
import com.example.maps.common.models.Restaurantes
import com.example.maps.common.repositories.PedidoRepository
import com.example.maps.common.repositories.RestaurantRepository

class RestauranteViewModel(application: Application) : AndroidViewModel(application) {

    private val _listaRestaurantes = MutableLiveData<List<Restaurantes>>()  // LiveData para la lista de restaurantes
    val listaRestaurantes: LiveData<List<Restaurantes>> get() = _listaRestaurantes

    private val _restauranteDetalle = MutableLiveData<Restaurantes?>() // LiveData para el detalle de un restaurante
    val restauranteDetalle: MutableLiveData<Restaurantes?> get() = _restauranteDetalle

    private val _carrito = MutableLiveData<MutableList<Pair<ProductosEnCarrito, Int>>>()
    val carrito: LiveData<MutableList<Pair<ProductosEnCarrito, Int>>> get() = _carrito

    private val _restauranteIdActual = MutableLiveData<Long?>() // Restaurante del carrito actual
    val restauranteIdActual: LiveData<Long?> get() = _restauranteIdActual

    private val _pedidosEnProceso = MutableLiveData<MutableList<PedidoCompleto>>()
    val pedidosEnProceso: LiveData<MutableList<PedidoCompleto>> get() = _pedidosEnProceso

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        _carrito.value = mutableListOf()
    }
    private val restaurantRepository = RestaurantRepository(application)
    private val pedidoRepository = PedidoRepository(application)

    fun getMisPedidos(token: String){
        pedidoRepository.getMisPedidos(token,
            onSuccess = { pedidos ->
                _pedidosEnProceso.postValue(pedidos.toMutableList())
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
            }
        )
    }

    fun postPedido(token: String, pedidoFinal: PedidoCrear, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        pedidoRepository.createPedido(token, pedidoFinal,
            onSuccess = {
                Log.d("PedidoViewModel", "Pedido creado exitosamente")
                onSuccess() // Llamar al callback de éxito
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
                onError(error) // Llamar al callback de error
            }
        )
    }

    fun agregarOEliminarDelCarrito(producto: ProductosEnCarrito, restauranteId: Long) {
        val listaActual = _carrito.value ?: mutableListOf()
        val restauranteActual = _restauranteIdActual.value

        // Validar que los productos sean del mismo restaurante
        if (restauranteActual != null && restauranteActual != restauranteId) {
            _errorMessage.postValue("Solo puedes agregar productos del mismo restaurante.")
            return
        }

        // Si es el primer producto, asignar el restaurante al carrito
        if (restauranteActual == null) {
            _restauranteIdActual.postValue(restauranteId)
        }

        val indiceExistente = listaActual.indexOfFirst { it.first.id == producto.id }

        if (indiceExistente != -1) {
            // Eliminar producto del carrito si ya existe
            listaActual.removeAt(indiceExistente)
        } else {
            // Agregar nuevo producto al carrito
            listaActual.add(Pair(producto, 1))
        }

        _carrito.value = listaActual
    }

    fun actualizarCantidadCarrito(itemCrear: PedidoItemCrear, nuevaCantidad: Int) {
        val listaActual = _carrito.value ?: mutableListOf()
        val index = listaActual.indexOfFirst { it.first.id == itemCrear.product_id }

        if (index != -1) {
            if (nuevaCantidad > 0) {
                listaActual[index] = Pair(
                    listaActual[index].first.copy(price = itemCrear.price),
                    nuevaCantidad
                )
            } else {
                listaActual.removeAt(index)
            }
        } else if (nuevaCantidad > 0) {
            listaActual.add(
                Pair(
                    ProductosEnCarrito(
                        id = itemCrear.product_id,
                        name = "",
                        price = itemCrear.price,
                        description = null.toString(),
                        restaurant_id = 0,
                        image = null
                    ),
                    nuevaCantidad
                )
            )
        }
        _carrito.value = listaActual
    }


    fun generarPedidoFinal(restaurantId: Long, total: Float, address: String, latitude: String, longitude: String): PedidoCrear {
        val detalles = _carrito.value?.map { (producto, cantidad) ->
            PedidoItemCrear(
                product_id = producto.id,
                qty = cantidad.toLong(),
                price = producto.price
            )
        } ?: emptyList()

        return PedidoCrear(
            restaurant_id = restaurantId,
            total = total,
            address = address,
            latitude = latitude,
            longitude = longitude,
            details = detalles
        )
    }


    fun limpiarCarrito() {
        _carrito.value = mutableListOf()
        _restauranteIdActual.value = null
    }

    // Función para obtener la lista de restaurantes
    fun obtenerRestaurantes(token: String) {
        restaurantRepository.fetchRestaurantes( token,
            onSuccess = { restaurantes ->
                _listaRestaurantes.value = restaurantes
                _errorMessage.postValue(null)
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
            }
        )
    }

    // Función para obtener el detalle de un restaurante
    fun obtenerRestauranteDetalle(token: String, id: Long) {
        restaurantRepository.fetchRestauranteDetalle(
            token,
            id,
            onSuccess = { restaurante ->
                _restauranteDetalle.postValue(restaurante)
            },
            onError = { error ->
                _restauranteDetalle.postValue(null) // Maneja el error adecuadamente
            }
        )
    }



}
