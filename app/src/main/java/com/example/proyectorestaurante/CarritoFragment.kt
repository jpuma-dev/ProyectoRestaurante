package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarritoFragment : Fragment(R.layout.fragment_carrito) {

    private lateinit var tvSubtotal: TextView
    private lateinit var adapter: CarritoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSubtotal = view.findViewById(R.id.tvSubtotal)

        val rv = view.findViewById<RecyclerView>(R.id.rvCarrito)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = CarritoAdapter(CartManager.getItems()) {

            adapter.notifyDataSetChanged()
            refreshUI(view)
        }
        rv.adapter = adapter

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        view.findViewById<TextView>(R.id.btnVaciar).setOnClickListener {
            CartManager.clear()
            adapter.notifyDataSetChanged()
            refreshUI(view)
        }


        view.findViewById<View>(R.id.btnPagar).setOnClickListener {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(requireContext(), "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            findNavController().navigate(R.id.pedidoFinalFragment)
        }

        refreshUI(view)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()

        refreshUI(requireView())
    }

    private fun refreshUI(root: View) {
        tvSubtotal.text = "Subtotal S/ %.2f".format(CartManager.total())


        val bottomBar = root.findViewById<View>(R.id.bottomBar)
        bottomBar.visibility = if (CartManager.getItems().isEmpty()) View.GONE else View.VISIBLE
    }
}