package com.example.proyectorestaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectorestaurante.databinding.ItemWelcomeBinding

class WelcomeAdapter(
    private val pages: List<WelcomePage>
) : RecyclerView.Adapter<WelcomeAdapter.PageVH>() {

    inner class PageVH(val binding: ItemWelcomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageVH {
        val binding = ItemWelcomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PageVH(binding)
    }

    override fun onBindViewHolder(holder: PageVH, position: Int) {
        val page = pages[position]

        holder.binding.portadaImgView.setImageResource(page.imageRes)
        holder.binding.tituloTxtView.text = page.title
        holder.binding.DescripciontextView.text = page.desc
    }

    override fun getItemCount() = pages.size
}
