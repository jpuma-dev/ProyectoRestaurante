package com.example.proyectorestaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CarritoAdapter(
    private val items: List<ProductoUI>,
    private val onChange: () -> Unit
) : RecyclerView.Adapter<CarritoAdapter.VH>() {

    inner class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
    ) {
        val img: ImageView = itemView.findViewById(R.id.imgProducto)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)

        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.tvNombre.text = item.nombre
        holder.tvCantidad.text = item.cantidad.toString()


        holder.tvPrecio.text = "S/ %.2f".format(item.precio * item.cantidad)

        Glide.with(holder.itemView)
            .load(item.imagenUrl)
            .placeholder(R.drawable.food_header)
            .into(holder.img)

        holder.btnPlus.setOnClickListener {
            CartManager.increase(item.id)
            notifyDataSetChanged()
            onChange()
        }

        holder.btnMinus.setOnClickListener {
            CartManager.decrease(item.id)
            notifyDataSetChanged()
            onChange()
        }

        holder.btnDelete.setOnClickListener {

            while (items.find { it.id == item.id } != null) {
                CartManager.decrease(item.id)
            }
            notifyDataSetChanged()
            onChange()
        }
    }
}