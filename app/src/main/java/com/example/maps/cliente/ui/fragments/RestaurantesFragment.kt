package com.example.maps.cliente.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maps.R
import com.example.maps.cliente.ui.adapters.RestauranteAdapter
import com.example.maps.common.ui.viewmodels.UserViewModel
import com.example.maps.databinding.FragmentRestaurantesBinding
import com.example.maps.restaurante.ui.viewmodels.RestauranteViewModel

class RestaurantesFragment : Fragment() {
    private var token: String = ""
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentRestaurantesBinding
    private val restauranteViewModel: RestauranteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantesBinding.inflate(inflater, container, false)
        token = userViewModel.userToken.value.toString()

        setupRecyclerView()
        setupEventListeners()
        setupObservers()
        restauranteViewModel.obtenerRestaurantes(token)
        return binding.root
    }

    private fun setupEventListeners() {
        val adapter = binding.rvRestaurantes.adapter as RestauranteAdapter
        restauranteViewModel.limpiarCarrito()
        adapter.onItemClick = { restaurante ->
            val bundle = Bundle().apply {
                putLong("id", restaurante.id) }
            findNavController().navigate(R.id.action_restaurantesFragment_to_restauranteMenuFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        val adapter = RestauranteAdapter(listOf()) { restaurante ->
            restauranteViewModel.obtenerRestauranteDetalle(token, restaurante.id)
        }
        binding.rvRestaurantes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRestaurantes.adapter = adapter
    }

    private fun setupObservers() {
        restauranteViewModel.listaRestaurantes.observe(viewLifecycleOwner, Observer { restaurantes ->
            (binding.rvRestaurantes.adapter as RestauranteAdapter).actualizarListaRestaurantes(restaurantes)
        })
        restauranteViewModel.restauranteDetalle.observe(viewLifecycleOwner, Observer { restaurante ->
            Log.d("RestaurantesFragment", "Detalle del restaurante: $restaurante")
        })
    }
}
