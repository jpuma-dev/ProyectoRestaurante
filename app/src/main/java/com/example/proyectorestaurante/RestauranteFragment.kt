package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class RestauranteFragment : Fragment(R.layout.fragment_restaurante) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
        val direccion = prefs.getString("direccion", "Sin dirección")
        view.findViewById<TextView>(R.id.tvDireccion).text = "📍 $direccion"

        val btnCart = view.findViewById<View>(R.id.btnCart)
        val tvCartBadge = view.findViewById<TextView>(R.id.tvCartBadge)

        fun updateCartBadge() {
            val count = CartManager.countTotalItems()
            if (count <= 0) {
                tvCartBadge.visibility = View.GONE
            } else {
                tvCartBadge.visibility = View.VISIBLE
                tvCartBadge.text = if (count > 99) "99+" else count.toString()
            }
        }

        btnCart.setOnClickListener {
            findNavController().navigate(R.id.carritoFragment)
        }



        fun goPlatos() {
            val b = Bundle().apply { putString("tipo", "platos") }
            findNavController().navigate(R.id.restauranteListaFragment, b)
        }

        fun goBebidas() {
            val b = Bundle().apply { putString("tipo", "bebidas") }
            findNavController().navigate(R.id.restauranteListaFragment, b)
        }

        fun goCombos() {
            findNavController().navigate(R.id.combosFragment)
        }

        fun goCarrito() {
            findNavController().navigate(R.id.carritoFragment)
        }

        fun goDetalle(item: ProductoUI) {
            val b = Bundle().apply {
                putString("id", item.id)
                putString("nombre", item.nombre)
                putDouble("precio", item.precio)
                putString("img", item.imagenUrl)
            }
            findNavController().navigate(R.id.detallePlatoFragment, b)
        }


        view.findViewById<MaterialCardView>(R.id.btnAtajoCombos).setOnClickListener { goCombos() }
        view.findViewById<MaterialCardView>(R.id.btnAtajoPlatos).setOnClickListener { goPlatos() }
        view.findViewById<MaterialCardView>(R.id.btnAtajoBebidas).setOnClickListener { goBebidas() }


        view.findViewById<TextView>(R.id.btnVerMasCombos).setOnClickListener { goCombos() }
        view.findViewById<TextView>(R.id.btnVerMasPlatos).setOnClickListener { goPlatos() }
        view.findViewById<TextView>(R.id.btnVerMasBebidas).setOnClickListener { goBebidas() }




        val rvCombos = view.findViewById<RecyclerView>(R.id.rvPreviewCombos)
        val rvPlatos = view.findViewById<RecyclerView>(R.id.rvPreviewPlatos)
        val rvBebidas = view.findViewById<RecyclerView>(R.id.rvPreviewBebidas)

        rvCombos.layoutManager = LinearLayoutManager(requireContext())
        rvPlatos.layoutManager = LinearLayoutManager(requireContext())
        rvBebidas.layoutManager = LinearLayoutManager(requireContext())


        val combos = listOf(
            ProductoUI(id = "combo_pb", nombre = "Combo: Plato + Bebida", precio = 29.90, imagenUrl = null),
            ProductoUI(id = "combo_pbp", nombre = "Combo: Plato + Bebida + Postre", precio = 37.90, imagenUrl = null)
        )

        rvCombos.adapter = ProductoAdapter(
            combos,
            onItemClick = { goCombos() },
            onAddClick = { goCombos() }
        )


        val bebidas = bebidasLocal()
        rvBebidas.adapter = ProductoAdapter(
            bebidas,
            onItemClick = { item -> goDetalle(item) },
            onAddClick = { item ->

                CartManager.add(item.copy(cantidad = 1))
                goCarrito()
            }
        )


        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.api.filterByCategory("Chicken")
                val platos = resp.meals.orEmpty().take(6).map { meal ->
                    ProductoUI(
                        id = meal.idMeal,
                        nombre = meal.strMeal,
                        precio = 16.90,
                        imagenUrl = meal.strMealThumb
                    )
                }

                rvPlatos.adapter = ProductoAdapter(
                    platos,
                    onItemClick = { item -> goDetalle(item) },
                    onAddClick = { item ->

                        CartManager.add(item.copy(cantidad = 1))
                        Toast.makeText(requireContext(), "Agregado: ${item.nombre}", Toast.LENGTH_SHORT).show()
                        goCarrito()
                    }
                )

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error platos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        updateCartBadge()


        setupToggle(view, R.id.btnToggleCombos, R.id.rvPreviewCombos)
        setupToggle(view, R.id.btnTogglePlatos, R.id.rvPreviewPlatos)
        setupToggle(view, R.id.btnToggleBebidas, R.id.rvPreviewBebidas)
    }
    override fun onResume() {
        super.onResume()
        view?.findViewById<TextView>(R.id.tvCartBadge)?.let {
            val count = CartManager.countTotalItems()
            it.visibility = if (count <= 0) View.GONE else View.VISIBLE
            it.text = if (count > 99) "99+" else count.toString()
        }
    }
    private fun bebidasLocal(): List<ProductoUI> = listOf(
        ProductoUI(id = "beb1", nombre = "Agua sin gas", precio = 2.90, imagenUrl = null),
        ProductoUI(id = "beb2", nombre = "Gaseosa personal", precio = 4.50, imagenUrl = null),
        ProductoUI(id = "beb3", nombre = "Jugo natural", precio = 6.50, imagenUrl = null),
        ProductoUI(id = "beb4", nombre = "Chicha morada", precio = 7.00, imagenUrl = null)
    )

    private fun setupToggle(view: View, btnId: Int, rvId: Int) {
        val btn = view.findViewById<ImageButton?>(btnId) ?: return
        val rv = view.findViewById<RecyclerView?>(rvId) ?: return

        fun update() {
            btn.setImageResource(
                if (rv.visibility == View.VISIBLE) R.drawable.ic_expand_less
                else R.drawable.ic_expand_more
            )
        }

        btn.setOnClickListener {
            rv.visibility = if (rv.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            update()
        }
        update()

    }
}