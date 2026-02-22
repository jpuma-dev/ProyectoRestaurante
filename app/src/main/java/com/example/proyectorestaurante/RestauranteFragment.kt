package com.example.proyectorestaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestauranteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestauranteFragment : Fragment(R.layout.fragment_restaurante) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dirección desde SharedPreferences
        val prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
        val direccion = prefs.getString("direccion", "Sin dirección")
        view.findViewById<TextView>(R.id.tvDireccion).text = "📍 $direccion"

        fun goLista(tipo: String) {
            val b = Bundle().apply { putString("tipo", tipo) }
            findNavController().navigate(R.id.restauranteListaFragment, b)
        }

        // Atajos
        view.findViewById<MaterialCardView>(R.id.btnAtajoCombos).setOnClickListener { goLista("combos") }
        view.findViewById<MaterialCardView>(R.id.btnAtajoPlatos).setOnClickListener { goLista("platos") }
        view.findViewById<MaterialCardView>(R.id.btnAtajoBebidas).setOnClickListener { goLista("bebidas") }

        // Ver más
        view.findViewById<TextView>(R.id.btnVerMasCombos).setOnClickListener { goLista("combos") }
        view.findViewById<TextView>(R.id.btnVerMasPlatos).setOnClickListener { goLista("platos") }
        view.findViewById<TextView>(R.id.btnVerMasBebidas).setOnClickListener { goLista("bebidas") }
    }
}


