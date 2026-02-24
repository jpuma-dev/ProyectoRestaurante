package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class DetallePlatoFragment : Fragment(R.layout.fragment_detalle_plato) {

    private var cantidad = 1
    private var precioUnit = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id") ?: ""
        val nombre = arguments?.getString("nombre") ?: "Producto"
        precioUnit = arguments?.getDouble("precio") ?: 0.0
        val img = arguments?.getString("img")

        val imgHeader = view.findViewById<ImageView>(R.id.imgHeader)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombre)
        val tvPrecioUnit = view.findViewById<TextView>(R.id.tvPrecioUnit)
        val tvCantidad = view.findViewById<TextView>(R.id.tvCantidad)
        val btnMinus = view.findViewById<ImageButton>(R.id.btnMinus)
        val btnPlus = view.findViewById<ImageButton>(R.id.btnPlus)
        val btnAgregar = view.findViewById<MaterialButton>(R.id.btnAgregarGrande)

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        tvNombre.text = nombre
        tvPrecioUnit.text = "S/ %.2f".format(precioUnit)

        Glide.with(view)
            .load(img)
            .placeholder(R.mipmap.ic_launcher)
            .into(imgHeader)

        fun updateBoton() {
            tvCantidad.text = cantidad.toString()
            val total = precioUnit * cantidad
            btnAgregar.text = "Agregar S/ %.2f".format(total)
        }

        btnMinus.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                updateBoton()
            }
        }

        btnPlus.setOnClickListener {
            cantidad++
            updateBoton()
        }

        updateBoton()


        btnAgregar.setOnClickListener {
            val producto = ProductoUI(
                id = id,
                nombre = nombre,
                precio = precioUnit,
                imagenUrl = img,
                cantidad = 1
            )

            repeat(cantidad) {
                CartManager.add(producto)
            }

            findNavController().navigate(R.id.carritoFragment)
        }
    }
}