package com.example.maps.chofer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maps.chofer.ui.adapters.PedidosLibresAdapter
import com.example.maps.databinding.FragmentPedidosLibresBinding
import com.example.maps.chofer.ui.viewmodels.ChoferViewModel
import com.example.maps.common.ui.viewmodels.UserViewModel

class PedidosLibresFragment : Fragment() {
    private var token: String = ""
    private val userViewModel: UserViewModel by activityViewModels()
    private val choferViewModel: ChoferViewModel by activityViewModels()
    private lateinit var binding: FragmentPedidosLibresBinding
    private lateinit var adapter: PedidosLibresAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPedidosLibresBinding.inflate(inflater, container, false)
        token = userViewModel.userToken.value.toString()
        setupRecyclerView()
        setupEventListeners()
        setupObservers()

        if (token.isNotEmpty()) {
            choferViewModel.getPedidosLibres(token)
        } else {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setupEventListeners() {

    }

    private fun setupRecyclerView() {
        adapter = PedidosLibresAdapter(mutableListOf(), choferViewModel, token)
        binding.recyclerViewPedidosLibres.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPedidosLibres.adapter = adapter
    }


    private fun setupObservers() {
        choferViewModel.pedidosLibres.observe(viewLifecycleOwner) { pedidos ->
            if (pedidos.isNotEmpty()) {
                adapter.updatePedidos(pedidos)
            } else {
                Toast.makeText(requireContext(), "No hay pedidos disponibles", Toast.LENGTH_SHORT).show()
            }
        }
        choferViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
