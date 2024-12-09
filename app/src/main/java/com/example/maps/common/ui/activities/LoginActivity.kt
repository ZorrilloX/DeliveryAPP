package com.example.maps.common.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.maps.cliente.ui.activities.ClienteActivity
import com.example.maps.chofer.ui.activities.ChoferActivity
import com.example.maps.common.authToken.TokenProvider
import com.example.maps.common.ui.viewmodels.UserViewModel
import com.example.maps.databinding.ActivityCommLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommLoginBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Mostrar formulario de registro
        binding.btnMostrarRegistro.setOnClickListener {
            binding.formularioLogin.visibility = View.GONE
            binding.btnMostrarRegistro.visibility = View.GONE
            binding.formularioRegistro.visibility = View.VISIBLE
            binding.btnMostrarLogin.visibility = View.VISIBLE
        }
        // Mostrar formulario de Login
        binding.btnMostrarLogin.setOnClickListener {
            binding.formularioLogin.visibility = View.VISIBLE
            binding.btnMostrarRegistro.visibility = View.VISIBLE
            binding.formularioRegistro.visibility = View.GONE
            binding.btnMostrarLogin.visibility = View.GONE
        }


        // Observar los cambios en el token
        userViewModel.userToken.observe(this) { token ->
            // Guardar el token
            TokenProvider(this).saveToken(token)

            // Obtener detalles del usuario
            token?.let { userViewModel.fetchUserDetails(it) }
        }

        // Observar los detalles del usuario para ingresar
        userViewModel.userData.observe(this) { usuario ->
            usuario?.let {
                // Navegar a la pantalla adecuada según el rol del usuario
                val intent = if (it.profile?.role == 1L) {
                    Intent(this, ClienteActivity::class.java)
                } else {
                    Intent(this, ChoferActivity::class.java)
                }
                // Pasar los datos básicos en el Intent
                intent.putExtra("name", it.name)
                intent.putExtra("email", it.email)
                intent.putExtra("role", it.profile?.role)
                intent.putExtra("id", it.id)
                startActivity(intent)
                finish()
            }
        }


        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                userViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.editTextNombreRegister.text.toString()
            val email = binding.editTextEmailRegister.text.toString()
            val password = binding.editTextPasswordRegister.text.toString()
            val role = if (binding.radioCliente.isChecked) 1 else 2 // 1 = Cliente, 2 = Chofer

            if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                // Aquí puedes realizar la lógica de registro (e.g., llamada a API)
                Toast.makeText(this, "Registrado como ${if (role == 1) "Cliente" else "Chofer"}", Toast.LENGTH_SHORT).show()
                // Ocultar formulario después de registrar
                binding.formularioRegistro.visibility = View.GONE
                binding.btnMostrarRegistro.visibility = View.VISIBLE
                userViewModel.registerUser(nombre, email, password, role.toLong())
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
