package com.example.maps.common.repositories

import android.content.Context
import com.example.maps.common.authToken.TokenProvider
import com.example.maps.common.api.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRepository(context: Context) {
    private val tokenProvider = TokenProvider(context)

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenProvider))
        .build()

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://proyectodelivery.jmacboy.com/api/")
            .build()
    }
}
