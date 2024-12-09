package com.example.maps.chofer.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.maps.common.models.PedidoCompleto
import com.example.maps.common.repositories.PedidoRepository

class ChoferViewModel(application: Application) : AndroidViewModel(application) {

    private val _pedidosLibres = MutableLiveData<MutableList<PedidoCompleto>>()
    val pedidosLibres: LiveData<MutableList<PedidoCompleto>> get() = _pedidosLibres

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val pedidoRepository = PedidoRepository(application)

    fun getPedidosLibres(token: String) {
        pedidoRepository.getPedidosLibres(token,
            onSuccess = { pedidos ->
                _pedidosLibres.postValue(pedidos.toMutableList())
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
            }
        )
    }

    fun tomarPedido(token: String, pedidoId: Long, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        pedidoRepository.aceptarPedido(token, pedidoId,
            onSuccess = {
                onSuccess()
                getPedidosLibres(token)
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
                onError(error)
            }
        )
    }

}
