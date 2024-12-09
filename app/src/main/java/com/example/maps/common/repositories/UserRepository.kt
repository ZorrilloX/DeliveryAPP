package com.example.maps.common.repositories

import android.util.Log
import com.example.maps.common.api.DeliveryApiService
import com.example.maps.common.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import com.example.maps.common.models.LoginRequest
import com.example.maps.common.models.LoginResponse
import com.example.maps.common.models.UsuarioRegistar

class UserRepository(context: Context) {

    private val api =
        RetrofitRepository(context).getRetrofitInstance().create(DeliveryApiService::class.java)

    fun registerUser(
        nombre: String,
        email: String,
        password: String,
        role: Long,
        onSuccess: (Usuario) -> Unit,
        onError: (Throwable) -> Unit) {

        val usuarioData = UsuarioRegistar(name = nombre,email = email,  password = password,role = role)

        api.registerUser(usuarioData).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("DeliveryRepository", "Error al registrar usuario: $errorBody")
                    onError(Throwable("Error al registrar usuario: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.e("DeliveryRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit, // Cambiado a String para devolver el token
        onError: (Throwable) -> Unit
    ) {
        val loginRequest = LoginRequest(email = email, password = password)

        api.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> { // Usamos LoginResponse para el token
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                println("Request enviado: $loginRequest")
                println("Response recibido: $response")

                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        val token = loginResponse.access_token // Extraemos el token
                        println("TOKEN: $token")
                        onSuccess(token) // Devolvemos el token al callback
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("DeliveryRepository", "Error al iniciar sesión: $errorBody")
                    onError(Throwable("Error al iniciar sesión: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("DeliveryRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }



    fun fetchUserDetails(
        token: String,
        onSuccess: (Usuario) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.getUserData("Bearer $token").enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UserRepository", "Error al obtener datos del usuario: $errorBody")
                    onError(Throwable("Error al obtener datos: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.e("UserRepository", "Fallo en la conexión: ${t.message}")
                onError(t)
            }
        })
    }

}
