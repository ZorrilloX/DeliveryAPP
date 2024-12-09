package com.example.maps.cliente.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maps.R
import com.example.maps.common.models.PedidoCompleto
import com.example.maps.databinding.ItemMisPedidosBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MisPedidosAdapter(private var pedidos: List<PedidoCompleto>) :
    RecyclerView.Adapter<MisPedidosAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemMisPedidosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.bind(pedido, position)
    }

    override fun getItemCount(): Int = pedidos.size

    inner class PedidoViewHolder(private val binding: ItemMisPedidosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: PedidoCompleto, position: Int) {
            //castear la fecha
            val dateFormatReceived = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val date = dateFormatReceived.parse(pedido.created_at)
            val dateFormatDisplay = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.txtFechaHora.text = date?.let { dateFormatDisplay.format(it) }
            binding.txtDireccion.text = pedido.address
            binding.txtEstado.text = getStatusDescription(pedido.status)


            val restaurantImg = pedido.order_details.firstOrNull()?.product?.image
            if (restaurantImg != null) {
                Glide.with(binding.imgFotoRestaurante.context)
                    .load(restaurantImg)
                    .placeholder(R.drawable.restaurant_default)
                    .into(binding.imgFotoRestaurante)
            } else {
                binding.imgFotoRestaurante.setImageResource(R.drawable.restaurant_default)
            }
        }

        private fun getStatusDescription(status: String): String {
            return when (status) {
                "1" -> "Solicitado"
                "2" -> "Aceptado por el Chofer"
                "3" -> "EN CAMINO..."
                "4" -> "Tu pedido ha sido Entregado"
                else -> "Desconocido"
            }
        }
    }
    // Método para actualizar la lista de pedidos
    @SuppressLint("NotifyDataSetChanged")
    fun updatePedidos(newPedidos: List<PedidoCompleto>) {
        this.pedidos = newPedidos
        notifyDataSetChanged()
    }
}
