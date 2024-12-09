package com.example.maps.cliente.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maps.cliente.ui.adapters.MenuAdapter
import com.example.maps.common.ui.viewmodels.UserViewModel
import com.example.maps.databinding.FragmentRestauranteMenuBinding
import com.example.maps.restaurante.ui.viewmodels.RestauranteViewModel

class EjemploFragment : Fragment() {
    private var token: String = ""
    private var restauranteId: Long = 0
    private val userViewModel: UserViewModel by activityViewModels()
    private val restauranteViewModel: RestauranteViewModel by activityViewModels()
    private lateinit var binding: FragmentRestauranteMenuBinding


}
