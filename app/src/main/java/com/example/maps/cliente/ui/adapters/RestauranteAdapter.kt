package com.example.maps.cliente.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maps.R
import com.example.maps.common.models.Restaurantes
import com.example.maps.databinding.ActivityClienteRestaurantItemBinding

class RestauranteAdapter(
    private var listaRestaurantes: List<Restaurantes>,
    var onItemClick: (Restaurantes) -> Unit // No pasamos el ViewModel ni el token aquí
) : RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
        val binding = ActivityClienteRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestauranteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {
        val restaurante = listaRestaurantes[position]

        holder.bind(restaurante)
        holder.itemView.setOnClickListener {
            onItemClick(restaurante)
        }
    }

    override fun getItemCount(): Int = listaRestaurantes.size

    inner class RestauranteViewHolder(private val binding: ActivityClienteRestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurante: Restaurantes) {
            // Usamos Glide para cargar la imagen
            val imageUrl = restaurante.logo ?: R.drawable.ic_launcher_foreground
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.restaurant_default)
                .into(binding.imageViewRestaurante)

            // Asignamos los datos de nombre y dirección
            binding.textViewNombreRestaurante.text = restaurante.name
            binding.textViewDireccionRestaurante.text = restaurante.address
        }
    }

    // Método para actualizar la lista de restaurantes
    @SuppressLint("NotifyDataSetChanged")
    fun actualizarListaRestaurantes(nuevaLista: List<Restaurantes>) {
        listaRestaurantes = nuevaLista
        notifyDataSetChanged()  // Notificamos al adapter que los datos han cambiado
    }
}
