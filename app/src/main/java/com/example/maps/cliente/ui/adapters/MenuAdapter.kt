package com.example.maps.cliente.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maps.R
import com.example.maps.common.models.ProductosEnCarrito
import com.example.maps.databinding.ItemMenuBinding

class MenuAdapter(
    private var productos: List<ProductosEnCarrito>,
    private var carrito: List<Pair<ProductosEnCarrito, Int>>, // Lo marcamos como mutable
    private val onProductClicked: (ProductosEnCarrito) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount() = productos.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateMenuItems(newProducts: List<ProductosEnCarrito>) {
        productos = newProducts
        notifyDataSetChanged()
    }

    fun updateCarrito(nuevoCarrito: List<Pair<ProductosEnCarrito, Int>>) {
        carrito = nuevoCarrito
        notifyDataSetChanged() // Notificar que el carrito ha cambiado
    }

    inner class MenuViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(producto: ProductosEnCarrito) {
            binding.tvNombreProducto.text = producto.name
            binding.tvPrecioProducto.text = producto.price.toString()
            binding.tvDescripcionProducto.text = producto.description

            val imageUrl = producto.image ?: R.drawable.food_default
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.food_default) // Imagen predeterminada
                .error(R.drawable.food_default) // Imagen en caso de error
                .into(binding.imgProducto)


            // Verificamos si el producto está en el carrito
            val isProductInCart = carrito.any { it.first.id == producto.id }

            if (isProductInCart) {
                binding.btnAnadirCarrito.text = "En el Carrito"
                binding.btnAnadirCarrito.setBackgroundColor(itemView.context.getColor(R.color.azul)) // Cambiar a un color adecuado
            } else {
                binding.btnAnadirCarrito.text = "Añadir al Carrito"
                binding.btnAnadirCarrito.setBackgroundColor(itemView.context.getColor(R.color.amarrillo)) // Color original
            }

            // Manejamos el clic del botón
            binding.btnAnadirCarrito.setOnClickListener {
                onProductClicked(producto) // Llamamos al callback para agregar o quitar del carrito
            }
        }
    }
}

