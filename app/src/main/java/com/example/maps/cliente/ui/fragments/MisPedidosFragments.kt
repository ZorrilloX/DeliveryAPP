package com.example.maps.cliente.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maps.cliente.ui.adapters.MisPedidosAdapter
import com.example.maps.common.ui.viewmodels.UserViewModel
import com.example.maps.databinding.FragmentMisPedidosBinding
import com.example.maps.restaurante.ui.viewmodels.RestauranteViewModel

class MisPedidosFragments : Fragment() {
    private var token: String = ""
    private val userViewModel: UserViewModel by activityViewModels()
    private val restauranteViewModel: RestauranteViewModel by activityViewModels()
    private lateinit var binding: FragmentMisPedidosBinding
    private lateinit var pedidosAdapter: MisPedidosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMisPedidosBinding.inflate(inflater, container, false)
        token = userViewModel.userToken.value.toString() ?: ""
        setupRecyclerView()
        setupObservers()

        // Opcional: Llama a getMisPedidos para obtener los pedidos
        restauranteViewModel.getMisPedidos(token)

        return binding.root
    }

    private fun setupRecyclerView() {
        pedidosAdapter = MisPedidosAdapter(emptyList()) // Inicialmente vacío
        binding.rvMisPedidos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pedidosAdapter
        }
    }

    private fun setupObservers() {
        restauranteViewModel.pedidosEnProceso.observe(viewLifecycleOwner) { pedidosEnProceso ->
            if (pedidosEnProceso.isNotEmpty()) {
                pedidosAdapter.updatePedidos(pedidosEnProceso)
            } else {
                // Manejar lista vacía, mostrar mensaje o placeholder
                binding.txtNoPedidos.visibility = View.VISIBLE
            }
        }
    }
}

