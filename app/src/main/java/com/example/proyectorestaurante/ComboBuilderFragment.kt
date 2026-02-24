package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ComboBuilderFragment : Fragment(R.layout.fragment_combo_builder) {

    private var platoSeleccionado: ProductoUI? = null
    private var bebidaSeleccionada: ProductoUI? = null
    private var postreSeleccionado: ProductoUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val comboTipoRaw = arguments?.getString("comboTipo")
        val comboTipo = comboTipoRaw?.trim()?.uppercase() ?: "PB"

        val rvPlatos = view.findViewById<RecyclerView>(R.id.rvPlatos)
        val rvBebidas = view.findViewById<RecyclerView>(R.id.rvBebidas)
        val rvPostres = view.findViewById<RecyclerView>(R.id.rvPostres)
        val btnConfirmar = view.findViewById<MaterialButton>(R.id.btnConfirmar)

        rvPlatos.layoutManager = LinearLayoutManager(requireContext())
        rvBebidas.layoutManager = LinearLayoutManager(requireContext())
        rvPostres.layoutManager = LinearLayoutManager(requireContext())


        val bebidas = bebidasLocal()
        rvBebidas.adapter = ProductoAdapter(
            bebidas,
            onItemClick = { beb ->
                bebidaSeleccionada = beb
                Toast.makeText(requireContext(), "Bebida: ${beb.nombre}", Toast.LENGTH_SHORT).show()
            },
            onAddClick = { beb ->
                bebidaSeleccionada = beb
                Toast.makeText(requireContext(), "Bebida: ${beb.nombre}", Toast.LENGTH_SHORT).show()
            }
        )


        lifecycleScope.launch {
            try {
                val respPlatos = RetrofitClient.api.filterByCategory("Chicken")
                val platos = respPlatos.meals.orEmpty().take(10).map { meal ->
                    ProductoUI(
                        id = meal.idMeal,
                        nombre = meal.strMeal,
                        precio = 16.90,
                        imagenUrl = meal.strMealThumb
                    )
                }

                rvPlatos.adapter = ProductoAdapter(
                    platos,
                    onItemClick = { pl ->
                        platoSeleccionado = pl
                        Toast.makeText(requireContext(), "Plato: ${pl.nombre}", Toast.LENGTH_SHORT).show()
                    },
                    onAddClick = { pl ->
                        platoSeleccionado = pl
                        Toast.makeText(requireContext(), "Plato: ${pl.nombre}", Toast.LENGTH_SHORT).show()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error platos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }


        if (comboTipo == "PBP") {
            rvPostres.visibility = View.VISIBLE
            view.findViewById<View>(R.id.tvPostre).visibility = View.VISIBLE

            lifecycleScope.launch {
                try {
                    val respPostres = RetrofitClient.api.filterByCategory("Dessert")
                    val postres = respPostres.meals.orEmpty().take(10).map { meal ->
                        ProductoUI(
                            id = "postre_${meal.idMeal}",
                            nombre = meal.strMeal,
                            precio = 7.90,
                            imagenUrl = meal.strMealThumb
                        )
                    }

                    rvPostres.adapter = ProductoAdapter(
                        postres,
                        onItemClick = { po ->
                            postreSeleccionado = po
                            Toast.makeText(requireContext(), "Postre: ${po.nombre}", Toast.LENGTH_SHORT).show()
                        },
                        onAddClick = { po -> // + pequeño también selecciona
                            postreSeleccionado = po
                            Toast.makeText(requireContext(), "Postre: ${po.nombre}", Toast.LENGTH_SHORT).show()
                        }
                    )
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error postres: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            rvPostres.visibility = View.GONE
            view.findViewById<View>(R.id.tvPostre).visibility = View.GONE
        }


        btnConfirmar.setOnClickListener {
            val plato = platoSeleccionado
            val bebida = bebidaSeleccionada

            if (plato == null || bebida == null) {
                Toast.makeText(requireContext(), "Selecciona un plato y una bebida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (comboTipo == "PBP" && postreSeleccionado == null) {
                Toast.makeText(requireContext(), "Selecciona un postre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val precioCombo = if (comboTipo == "PBP") 37.90 else 29.90

            val nombreCombo = if (comboTipo == "PBP") {
                "Combo: ${plato.nombre} + ${bebida.nombre} + ${postreSeleccionado!!.nombre}"
            } else {
                "Combo: ${plato.nombre} + ${bebida.nombre}"
            }


            val idCombo = if (comboTipo == "PBP") {
                "combo_pbp_${plato.id}_${bebida.id}_${postreSeleccionado!!.id}"
            } else {
                "combo_pb_${plato.id}_${bebida.id}"
            }


            val comboUI = ProductoUI(
                id = idCombo,
                nombre = nombreCombo,
                precio = precioCombo,
                imagenUrl = plato.imagenUrl
            )

            CartManager.add(comboUI)
            findNavController().navigate(R.id.carritoFragment)
            Toast.makeText(
                requireContext(),
                "Agregado al carrito ✅\nTotal: S/ %.2f".format(CartManager.total()),
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun bebidasLocal(): List<ProductoUI> {
        return listOf(
            ProductoUI(id="beb1", nombre="Agua sin gas", precio=2.90, imagenUrl=null),
            ProductoUI(id="beb2", nombre="Gaseosa personal", precio=4.50, imagenUrl=null),
            ProductoUI(id="beb3", nombre="Jugo natural", precio=6.50, imagenUrl=null),
            ProductoUI(id="beb4", nombre="Chicha morada", precio=7.00, imagenUrl=null)
        )
    }
}