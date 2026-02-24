package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class RestauranteListaFragment : Fragment(R.layout.fragment_restaurante_lista) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvCombos)
        rv.layoutManager = LinearLayoutManager(requireContext())

        val tipo = arguments?.getString("tipo")?.lowercase()?.trim()

        val header = view.findViewById<TextView>(R.id.tvCombosHeader)
        header.text = when (tipo) {
            "platos" -> "Platos del día"
            "bebidas" -> "Bebidas"
            else -> "Lista"
        }

        lifecycleScope.launch {
            try {
                val items: List<ProductoUI> = when (tipo) {

                    "bebidas" -> bebidasLocal()

                    "platos" -> {
                        val resp = RetrofitClient.api.filterByCategory("Chicken")
                        resp.meals.orEmpty().take(10).map { meal ->
                            ProductoUI(
                                id = meal.idMeal,
                                nombre = meal.strMeal,
                                precio = precioPlato(meal.strMeal),
                                imagenUrl = meal.strMealThumb
                            )
                        }
                    }

                    else -> emptyList()
                }

                rv.adapter = ProductoAdapter(
                    items,
                    onItemClick = { producto ->

                        val b = Bundle().apply {
                            putString("id", producto.id)
                            putString("nombre", producto.nombre)
                            putDouble("precio", producto.precio)
                            putString("img", producto.imagenUrl)
                        }
                        findNavController().navigate(R.id.detallePlatoFragment, b)
                    },
                    onAddClick = { producto ->

                        CartManager.add(producto.copy(cantidad = 1))
                        Toast.makeText(requireContext(), "Agregado: ${producto.nombre}", Toast.LENGTH_SHORT).show()
                    }
                )

                if (items.isEmpty()) {
                    Toast.makeText(requireContext(), "No hay datos para mostrar", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error API: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun bebidasLocal(): List<ProductoUI> {
        return listOf(
            ProductoUI(id = "beb1", nombre = "Agua sin gas", precio = 2.90, imagenUrl = null),
            ProductoUI(id = "beb2", nombre = "Gaseosa personal", precio = 4.50, imagenUrl = null),
            ProductoUI(id = "beb3", nombre = "Jugo natural", precio = 6.50, imagenUrl = null),
            ProductoUI(id = "beb4", nombre = "Chicha morada", precio = 7.00, imagenUrl = null)
        )
    }

    private fun precioPlato(nombre: String): Double {
        val n = nombre.lowercase()
        return when {
            "chicken" in n || "pollo" in n -> 16.90
            "beef" in n || "carne" in n -> 19.90
            "fish" in n -> 21.90
            else -> 18.90
        }
    }
}