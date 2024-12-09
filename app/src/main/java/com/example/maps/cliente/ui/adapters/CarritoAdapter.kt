package com.example.maps.cliente.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maps.R
import com.example.maps.common.models.PedidoItem
import com.example.maps.databinding.ItemCarritoBinding

class CarritoAdapter(private var items: List<PedidoItem>,
                     private val onCantidadChanged: (PedidoItem, Int) -> Unit) :
    RecyclerView.Adapter<CarritoAdapter.PedidoViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updatePedidoItems(newItems: List<PedidoItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class PedidoViewHolder(private val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PedidoItem) {
            binding.txtProductoNombre.text = item.name
            binding.txtProductoPrecio.text = "$${item.price}"
            binding.txtCantidad.text = item.quantity.toString()
            binding.txtProductoSubTotal.text = "$${item.price * item.quantity}"
            val imageUrl = item.image ?: R.drawable.food_default
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.food_default)
                .error(R.drawable.food_default)
                .into(binding.imgPedidoActual)


            binding.btnAumentar.setOnClickListener {
                val nuevaCantidad = item.quantity + 1
                onCantidadChanged(item, nuevaCantidad)
            }

            binding.btnDisminuir.setOnClickListener {
                if (item.quantity > 1) {
                    val nuevaCantidad = item.quantity - 1
                    onCantidadChanged(item, nuevaCantidad)
                }
            }
        }
    }
}

