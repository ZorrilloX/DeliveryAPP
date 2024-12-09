package com.example.maps.chofer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.maps.R
import com.example.maps.common.authToken.TokenProvider
import com.example.maps.common.models.Perfil
import com.example.maps.common.models.Usuario
import com.example.maps.common.ui.activities.LoginActivity
import com.example.maps.databinding.ActivityChoferBinding
import com.google.android.material.navigation.NavigationView

class ChoferActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityChoferBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userChofer: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDrawer()
        setupNavigationView()
        receiveIntentData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Pedidos para Chofer"
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupNavigationView() {
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_pedidos_libres // El fragmento que cargará los pedidos libres
            ), binding.drawerLayout
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun receiveIntentData() {
        val idUsuario = intent.getLongExtra("id", -1L)
        val nombreUsuario = intent.getStringExtra("name") ?: "Chofer"
        val emailUsuario = intent.getStringExtra("email")
        val roleUsuario = intent.getLongExtra("role", -1L)

        val perfil = Perfil(idUsuario, roleUsuario)
        userChofer = Usuario(idUsuario, nombreUsuario, emailUsuario, perfil)
        println("user chofer: $userChofer")

        binding.navView.menu.findItem(R.id.IDuserName).title = nombreUsuario
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_pedidos_libres -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.pedidosLibresFragment)
            }
            R.id.nav_cerrar_sesion -> {
                TokenProvider(this).clearToken()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
