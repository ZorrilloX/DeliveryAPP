package com.example.maps.common.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.maps.common.models.Usuario
import com.example.maps.common.repositories.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val _userData = MutableLiveData<Usuario>()    // LiveData para los datos del usuario
    val userData: LiveData<Usuario> get() = _userData

    private val _userToken = MutableLiveData<String>()     // LiveData para el token del usuario
    val userToken: LiveData<String> get() = _userToken

    private val _errorMessage = MutableLiveData<String?>() //mensajes de error
    val errorMessage: LiveData<String?> = _errorMessage

    private val userRepository = UserRepository(application)

    // Login para obtener el token
    fun loginUser(email: String, password: String) {
        userRepository.loginUser(email, password,
            onSuccess = { token ->
                // Guardamos el token cuando el login es exitoso
                _userToken.value = token
                _errorMessage.postValue(null)
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
            }
        )
    }

    // Función para obtener los detalles del usuario usando el token
    fun fetchUserDetails(token: String) {
        userRepository.fetchUserDetails(token,
            onSuccess = { user ->
                _userData.value = user
                _errorMessage.postValue(null)
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
            }
        )
    }

    //funcion para registrar usuario
    fun registerUser(name: String, email: String, password: String, rol: Long) {
        userRepository.registerUser(name, email, password, rol,
            onSuccess = { user ->
                // Guardamos el token cuando el login es exitoso
                _userData.value = user
                _errorMessage.postValue(null)
            },
            onError = { error ->
                _errorMessage.postValue(error.message)
            }
        )
    }
}
