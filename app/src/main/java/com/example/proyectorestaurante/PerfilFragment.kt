package com.example.proyectorestaurante

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<View>(R.id.optCerrarSesion).setOnClickListener {


            CartManager.clear()


            val prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
            prefs.edit().clear().apply()


            findNavController().popBackStack(R.id.menuHomeFragment, false)


        }
    }
}