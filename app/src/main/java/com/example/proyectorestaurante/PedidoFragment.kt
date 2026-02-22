package com.example.proyectorestaurante

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectorestaurante.databinding.FragmentPedidoBinding

class PedidoFragment : Fragment() {
    private var _binding: FragmentPedidoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        // Simulación de datos que vendrían del API
        val datosSimulados = WelcomePage(
            R.drawable.welcome_1,
            "Arroz chaufa.",
            "san isidro, Chorrillos 15067, Peru #12"
        )
        binding.tvTituloPedido.text = "Tu pedido"
        binding.tvTituloPedido.text = datosSimulados.title
        binding.tvDireccion.text = datosSimulados.desc


        configurarColoresMaquetado()
    }

    private fun configurarColoresMaquetado() {
        val verdeOscuro = Color.parseColor("#2D8A4E") // El verde de la barra
        val verdeClaro = Color.parseColor("#A8D5BA")  // El verde del botón

        binding.bottomBar.backgroundTintList = ColorStateList.valueOf(verdeOscuro)
        binding.btnHacerPedido.backgroundTintList = ColorStateList.valueOf(verdeClaro)
        binding.btnHacerPedido.setTextColor(verdeOscuro)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}