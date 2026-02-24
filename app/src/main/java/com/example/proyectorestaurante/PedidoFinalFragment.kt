package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import androidx.navigation.fragment.findNavController
class PedidoFinalFragment : Fragment(R.layout.fragment_pedido_final) {

    private lateinit var tvTotal: TextView
    private lateinit var tvDireccion: TextView
    private lateinit var tvEntregaEstimada: TextView
    private lateinit var rv: RecyclerView

    private var entregaModo: String = "INMEDIATA"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        tvDireccion = view.findViewById(R.id.tvDireccion)
        tvTotal = view.findViewById(R.id.tvTotal)
        tvEntregaEstimada = view.findViewById(R.id.tvEntregaEstimada)
        rv = view.findViewById(R.id.rvPedido)


        // Dirección
        val prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
        tvDireccion.text = prefs.getString("direccion", "Sin dirección")

        // Lista (resumen del carrito)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = PedidoFinalAdapter(
            items = CartManager.getItems(),
            onDelete = { id ->
                CartManager.remove(id)
                rv.adapter?.notifyDataSetChanged()
                updateTotal()
            }
        )
        val actMetodoPago = view.findViewById<MaterialAutoCompleteTextView>(R.id.actMetodoPago)

        val opcionesPago = listOf("Tarjeta", "Efectivo", "Yape")
        val adapterPago = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, opcionesPago)
        actMetodoPago.setAdapter(adapterPago)

// por defecto
        actMetodoPago.setText("Tarjeta", false)
        // Entrega: cards
        val cardInmediata = view.findViewById<MaterialCardView>(R.id.cardInmediata)
        val cardProgramada = view.findViewById<MaterialCardView>(R.id.cardProgramada)

        fun paintCards() {
            val orange = ContextCompat.getColor(requireContext(), R.color.orange_main)
            val gray = ContextCompat.getColor(requireContext(), android.R.color.darker_gray)

            if (entregaModo == "INMEDIATA") {
                cardInmediata.strokeWidth = 3
                cardInmediata.strokeColor = orange
                cardProgramada.strokeWidth = 2
                cardProgramada.strokeColor = gray
                tvEntregaEstimada.text = "30 - 45 min"
            } else {
                cardProgramada.strokeWidth = 3
                cardProgramada.strokeColor = orange
                cardInmediata.strokeWidth = 2
                cardInmediata.strokeColor = gray
                tvEntregaEstimada.text = "Programada"
            }
        }

        cardInmediata.setOnClickListener {
            entregaModo = "INMEDIATA"
            paintCards()
        }

        cardProgramada.setOnClickListener {
            entregaModo = "PROGRAMADA"
            paintCards()
            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
        }


        entregaModo = "INMEDIATA"
        paintCards()





        view.findViewById<MaterialButton>(R.id.btnHacerPedido).setOnClickListener {


            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(requireContext(), "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val metodo = actMetodoPago.text?.toString()?.trim().orEmpty()
            if (metodo.isEmpty()) {
                Toast.makeText(requireContext(), "Selecciona un método de pago", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            Toast.makeText(
                requireContext(),
                "Pedido realizado ✅\nEntrega: $entregaModo\nPago: $metodo\nTotal: S/ %.2f".format(CartManager.total()),
                Toast.LENGTH_LONG
            ).show()


            CartManager.clear()


            findNavController().navigate(R.id.graciasPedidoFragment)
        }
        updateTotal()
    }

    private fun updateTotal() {
        tvTotal.text = "Total S/ %.2f".format(CartManager.total())
    }

    override fun onResume() {
        super.onResume()
        rv.adapter?.notifyDataSetChanged()
        updateTotal()
    }
}