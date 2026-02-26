package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DetallePlatoFragment : Fragment(R.layout.fragment_detalle_plato) {
    private var cantidad = 1
    private var precioUnitario = 0.0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id") ?: ""

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getMealById(id)
                val meal = response.meals?.firstOrNull()

                view.findViewById<TextView>(R.id.tvDescripcion).text =
                    meal?.strInstructions ?: "Sin descripción disponible"

            } catch (e: Exception) {
                view.findViewById<TextView>(R.id.tvDescripcion).text =
                    "No se pudo cargar la descripción"
            }
        }



        val nombre = arguments?.getString("nombre") ?: ""
        val precio = arguments?.getDouble("precio") ?: 0.0
        val imagenUrl = arguments?.getString("img")


        val img = view.findViewById<ImageView>(R.id.imgDetalle)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreDetalle)
        val tvPrecio = view.findViewById<TextView>(R.id.tvPrecioDetalle)
        val btnAgregar = view.findViewById<MaterialButton>(R.id.btnAgregar)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val tvCantidad = view.findViewById<TextView>(R.id.tvCantidad)
        val btnMas = view.findViewById<ImageButton>(R.id.btnMas)
        val btnMenos = view.findViewById<ImageButton>(R.id.btnMenos)

        tvCantidad.text = cantidad.toString()

        fun actualizarVista() {
            val total = precioUnitario * cantidad

            tvCantidad.text = cantidad.toString()
            tvPrecio.text = "S/ %.2f".format(total)

            btnAgregar.text = "Agregar S/ %.2f".format(total)
        }

        precioUnitario = precio
        actualizarVista()

        btnMas.setOnClickListener {
            cantidad++
            actualizarVista()
        }

        btnMenos.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                actualizarVista()
            }
        }

        tvNombre.text = nombre
        tvPrecio.text = "S/ %.2f".format(precio)

        Glide.with(this)
            .load(imagenUrl)
            .placeholder(R.drawable.food_header)
            .into(img)



        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


        btnAgregar.setOnClickListener {

            val producto = ProductoUI(
                id = id,
                nombre = nombre,
                precio = precioUnitario,
                imagenUrl = imagenUrl,
                cantidad = cantidad
            )

            CartManager.add(producto)

            findNavController().navigate(R.id.carritoFragment)
        }



    }
}