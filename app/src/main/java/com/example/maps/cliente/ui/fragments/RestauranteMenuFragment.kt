package com.example.maps.cliente.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maps.R
import com.example.maps.cliente.ui.adapters.MenuAdapter
import com.example.maps.common.ui.viewmodels.UserViewModel
import com.example.maps.databinding.FragmentRestauranteMenuBinding
import com.example.maps.restaurante.ui.viewmodels.RestauranteViewModel

class RestauranteMenuFragment : Fragment() {
    private var token: String = ""
    private var restauranteId: Long = 0
    private val userViewModel: UserViewModel by activityViewModels()
    private val restauranteViewModel: RestauranteViewModel by activityViewModels()
    private lateinit var binding: FragmentRestauranteMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RestauranteMenuFragment", "ENTRANDO EN MENU")
        arguments?.let {
            restauranteId = it.getLong("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestauranteMenuBinding.inflate(inflater, container, false)
        token = userViewModel.userToken.value ?: ""

        setupRecyclerView()
        setupEventListeners()
        setupObservers()
        restauranteViewModel.obtenerRestauranteDetalle(token, restauranteId)

        return binding.root
    }

    private fun setupEventListeners() {
        binding.btnHacerPedido.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("restauranteId", restauranteId)
            }
            findNavController().navigate(R.id.action_restauranteMenuFragment_to_pedidoFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        val adapter = MenuAdapter(
            productos = listOf(), // Lista de productos inicialmente vacía
            carrito = restauranteViewModel.carrito.value ?: listOf() // Pasamos el carrito actual al adaptador
        ) { producto ->
            Log.d("RestauranteMenuFragment", "Producto añadido o eliminado del carrito: ${producto.name}")
            restauranteViewModel.agregarOEliminarDelCarrito(producto, restauranteId) // Cambiar para alternar entre añadir y eliminar
        }
        binding.rvMenuRestaurante.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenuRestaurante.adapter = adapter

        // Observar cambios en el carrito
        restauranteViewModel.carrito.observe(viewLifecycleOwner) { carrito ->
            (binding.rvMenuRestaurante.adapter as MenuAdapter).updateCarrito(carrito) // Actualizar el carrito
        }
    }



    private fun setupObservers() {
        restauranteViewModel.restauranteDetalle.observe(viewLifecycleOwner) { restaurante ->
            if (restaurante != null) {
                binding.tvNombreRestaurante.text = restaurante.name
                (binding.rvMenuRestaurante.adapter as MenuAdapter).updateMenuItems(restaurante.products)
            }
        }
    }
}
