package com.example.proyectorestaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PedidoFinalAdapter(
    private val items: List<ProductoUI>,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<PedidoFinalAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.imgProducto)
        val tvNombre: TextView = v.findViewById(R.id.tvNombre)
        val tvPrecio: TextView = v.findViewById(R.id.tvPrecio)
        val tvCantidad: TextView = v.findViewById(R.id.tvCantidad)
        val btnDelete: View = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido_final, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.tvNombre.text = item.nombre
        holder.tvPrecio.text = "S/ %.2f".format(item.precio)
        holder.tvCantidad.text = "x${item.cantidad}"

        if (item.imagenUrl != null) {
            Glide.with(holder.itemView)
                .load(item.imagenUrl)
                .into(holder.img)
        } else if (item.imagenRes != null) {
            holder.img.setImageResource(item.imagenRes)
        } else {
            holder.img.setImageResource(R.drawable.food_header)
        }

        holder.btnDelete.setOnClickListener { onDelete(item.id) }
    }

    override fun getItemCount(): Int = items.size
}