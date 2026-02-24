package com.example.proyectorestaurante

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton

class GraciasPedidoFragment : Fragment(R.layout.fragment_gracias_pedido) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.btnVolver).setOnClickListener {

            findNavController().popBackStack(R.id.menuHomeFragment, false)
        }
    }
}