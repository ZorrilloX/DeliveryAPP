package com.example.maps.common.models

data class Usuario(
    val id: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val profile: Perfil? = null
)

data class Perfil(
    val id: Long,
    val role: Long // 1 = Cliente, 2 = Chofer
)

data class UsuarioRegistar(
    val name: String,
    val email: String,
    val password: String,
    val role: Long
)


