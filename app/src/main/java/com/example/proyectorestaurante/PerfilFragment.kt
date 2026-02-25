package com.example.proyectorestaurante

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Card / opción "Cerrar Sesión" (asegúrate que tu XML tenga este id)
        view.findViewById<View>(R.id.optCerrarSesion).setOnClickListener {

            // 1) Limpia cosas locales (para que no se quede el carrito)
            CartManager.clear()

            // 2) (Opcional) borrar dirección / prefs si quieres
            val prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            // 3) Volver al inicio:
            //    ✅ Opción A (RECOMENDADA): volver al startDestination del nav
            //    Esto te regresa al inicio del flujo dentro del Navigation.
            findNavController().popBackStack(R.id.menuHomeFragment, false)

            // Si tu start real es Welcome/Intro, cámbialo por ese ID:
            // findNavController().popBackStack(R.id.welcomeFragment, false)
        }
    }
}