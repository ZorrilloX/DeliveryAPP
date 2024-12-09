package com.example.maps.chofer.ui.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maps.R
import com.example.maps.chofer.ui.activities.MapChoferActivity
import com.example.maps.chofer.ui.viewmodels.ChoferViewModel
import com.example.maps.common.models.PedidoCompleto
import com.example.maps.databinding.ItemPedidoLibreBinding

class PedidosLibresAdapter(
    private var pedidos: MutableList<PedidoCompleto>,
    private val choferViewModel: ChoferViewModel,
    private val token: String
) : RecyclerView.Adapter<PedidosLibresAdapter.PedidoViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updatePedidos(newPedidos: List<PedidoCompleto>) {
        pedidos.clear()
        pedidos.addAll(newPedidos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemPedidoLibreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.bind(pedido)
    }

    override fun getItemCount(): Int = pedidos.size

    inner class PedidoViewHolder(private val binding: ItemPedidoLibreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(pedido: PedidoCompleto) {
            binding.txtFechaHora.text = pedido.created_at
            binding.txtDireccion.text = pedido.address
            binding.txtEstado.text = getStatusDescription(pedido.status)
            binding.txtDineroPagar.text = "Dinero a Pagar: ${pedido.total}" // Asignar el dinero a pagar

            val firstProductImage = pedido.order_details.firstOrNull()?.product?.image
            if (firstProductImage != null) {
                Glide.with(binding.imgFotoRestaurante.context)
                    .load(firstProductImage)
                    .placeholder(R.drawable.food_default) // Imagen predeterminada
                    .into(binding.imgFotoRestaurante)
            } else {
                binding.imgFotoRestaurante.setImageResource(R.drawable.food_default) // Imagen predeterminada si no hay productos
            }

            binding.btnCogerPedido.setOnClickListener {
                choferViewModel.tomarPedido(token, pedido.id,
                    onSuccess = {
                        Toast.makeText(binding.root.context, "Pedido cogido: ${pedido.id}", Toast.LENGTH_SHORT).show()
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            pedidos.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        choferViewModel.getPedidosLibres(token)
                        val intent = Intent(binding.root.context, MapChoferActivity::class.java).apply {
                            putExtra("LATITUDE", pedido.latitude.toDouble())
                            putExtra("LONGITUDE", pedido.longitude.toDouble())
                        }
                        binding.root.context.startActivity(intent)
                    },
                    onError = { error ->
                        println("Error al coger pedido: ${error.message}")
                        Toast.makeText(binding.root.context, "Error al coger pedido: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                )

            }

        }

        private fun getStatusDescription(status: String): String {
            return when (status) {
                "1" -> "En preparación"
                "2" -> "En camino"
                "3" -> "Entregado"
                else -> "Desconocido"
            }
        }
    }
}
