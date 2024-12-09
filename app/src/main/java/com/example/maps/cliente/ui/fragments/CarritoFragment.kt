package com.example.maps.cliente.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maps.MapActivity
import com.example.maps.R
import com.example.maps.cliente.ui.adapters.CarritoAdapter
import com.example.maps.common.models.PedidoItem
import com.example.maps.common.models.PedidoItemCrear
import com.example.maps.common.ui.viewmodels.UserViewModel
import com.example.maps.databinding.FragmentClientePedidoBinding
import com.example.maps.restaurante.ui.viewmodels.RestauranteViewModel

class CarritoFragment : Fragment() {
    private var token: String = ""
    private val userViewModel: UserViewModel by activityViewModels()
    private val restauranteViewModel: RestauranteViewModel by activityViewModels()
    private lateinit var binding: FragmentClientePedidoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClientePedidoBinding.inflate(inflater, container, false)
        token = userViewModel.userToken.value ?: ""
        setupRecyclerView()
        setupEventListeners()
        setupObservers()

        return binding.root
    }

    private fun setupEventListeners() {
        binding.btnUbicacion.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, LOCATION_REQUEST_CODE)
        }

        binding.btnOrdenar.setOnClickListener {
            val restaurantId = restauranteViewModel.restauranteIdActual.value ?: return@setOnClickListener
            Log.d("total", binding.lblPrecioTotal.text.toString())
            val totalString = binding.lblPrecioTotal.text.toString().replace("Total: ", "").trim()
            Log.d("total2", totalString)
            val total = totalString.toFloat()
            val address = binding.lblMiUbicacionActual.text.toString() // Dirección obtenida del usuario
            val latitude = selectedLatitude ?: return@setOnClickListener // Latitude obtenida del GPS
            val longitude = selectedLongitude ?: return@setOnClickListener // Longitude obtenida del GPS

            val pedidoFinal = restauranteViewModel.generarPedidoFinal(
                restaurantId,
                total,
                address,
                latitude,
                longitude
            )
            // Hacer post con viewmodel
            restauranteViewModel.postPedido(token, pedidoFinal,
                onSuccess = {
                    Toast.makeText(requireContext(), "Pedido creado con éxito", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_pedidoFragment_to_restaurantesFragment2)
                },
                onError = { error ->
                    Toast.makeText(requireContext(), "Error al crear el pedido: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
            Log.d("PedidoFinal", pedidoFinal.toString()) // Aquí se imprime el objeto final
        }
    }

    private fun setupRecyclerView() {
        val adapter = CarritoAdapter(listOf()) { producto, cantidad ->
            // Convertimos el Producto a PedidoItemCrear antes de enviarlo a la vista
            val productoItemCrear = convertirAPedidoItemCrear(producto)
            // Lógica para actualizar el carrito, pasando PedidoItemCrear
            restauranteViewModel.actualizarCantidadCarrito(productoItemCrear, cantidad)
        }
        binding.recyclerViewCarrito.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCarrito.adapter = adapter
    }


    private fun setupObservers() {
        restauranteViewModel.carrito.observe(viewLifecycleOwner) { carrito ->
            if (carrito.isNotEmpty()) {
                binding.lblMiPedidoActual.text = "Mi Pedido Actual"
                binding.lblMiUbicacionActual.text = "Ubicación pendiente"

                var total = 0.0

                // Mapeamos el carrito para convertirlo a una lista de PedidoItem
                val items = carrito.map { (producto, cantidad) ->
                    val subtotal = producto.price.toDouble() * cantidad
                    total += subtotal
                    PedidoItem(
                        id = producto.id,
                        name = producto.name,
                        price = producto.price.toFloat(),
                        quantity = cantidad,
                        image = producto.image.toString()
                    )
                }

                // Actualizamos el precio total en el UI
                binding.lblPrecioTotal.text = "Total: $total"

                // Actualizamos el adaptador con la lista mapeada
                (binding.recyclerViewCarrito.adapter as CarritoAdapter).updatePedidoItems(items)
            }
        }
    }

    // Función para convertir un PedidoItem a un objeto Products
    private fun convertirAPedidoItemCrear(item: PedidoItem): PedidoItemCrear {
        return PedidoItemCrear(
            product_id = item.id,
            qty = item.quantity.toLong(),
            price = item.price.toString()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.let {
                val address = it.getStringExtra("ADDRESS") ?: ""
                val latitude = it.getStringExtra("LATITUDE") ?: ""
                val longitude = it.getStringExtra("LONGITUDE") ?: ""

                binding.lblMiUbicacionActual.text = address
                selectedLatitude = latitude
                selectedLongitude = longitude
            }
        }
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private var selectedLatitude: String? = null
        private var selectedLongitude: String? = null
    }
}
