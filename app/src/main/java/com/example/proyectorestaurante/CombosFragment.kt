package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView

class CombosFragment : Fragment(R.layout.fragment_combos) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialCardView>(R.id.cardCombo1).setOnClickListener {
            val b = Bundle().apply { putString("comboTipo", "PB") }
            findNavController().navigate(R.id.comboBuilderFragment, b)
        }

        view.findViewById<MaterialCardView>(R.id.cardCombo2).setOnClickListener {
            val b = Bundle().apply { putString("comboTipo", "PBP") }
            findNavController().navigate(R.id.comboBuilderFragment, b)
        }
    }
}