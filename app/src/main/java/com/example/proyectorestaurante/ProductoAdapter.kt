package com.example.proyectorestaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductoAdapter(
    private val items: List<ProductoUI>,
    private val onItemClick: (ProductoUI) -> Unit,
    private val onAddClick: (ProductoUI) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val imgProducto: ImageView = v.findViewById(R.id.imgProducto)
        val tvNombre: TextView = v.findViewById(R.id.tvNombre)
        val tvPrecio: TextView = v.findViewById(R.id.tvPrecio)
        val btnAdd: View = v.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_lista, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.tvNombre.text = item.nombre
        holder.tvPrecio.text = "S/ %.2f".format(item.precio)

        if (item.imagenRes != null) {
            holder.imgProducto.setImageResource(item.imagenRes)
        } else {
            Glide.with(holder.itemView)
                .load(item.imagenUrl)
                .placeholder(R.drawable.food_header)
                .into(holder.imgProducto)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        holder.btnAdd.setOnClickListener {
            onAddClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}