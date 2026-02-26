package com.example.proyectorestaurante

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectorestaurante.databinding.ActivityWelcomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var autoNavDone = false
    private lateinit var pageCallback: ViewPager2.OnPageChangeCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val pages = listOf(
            WelcomePage(
                R.drawable.welcome_1,
                "Restaurante",
                "Saludable y adaptado a ti. Consume las calorías y nutrientes que necesitas para lograr tu objetivo."
            ),
            WelcomePage(
                R.drawable.welcome_2,
                "Realiza tu pedido",
                "Selecciona tus platos favoritos desde el menú y agrégalos al carrito de manera rápida y sencilla."
            ),
            WelcomePage(
                R.drawable.welcome_3,
                "Entrega rápida",
                "Recibe tu pedido en la puerta de tu casa y disfruta de una experiencia rápida, segura y confiable."
            )
        )

        binding.viewPager.adapter = WelcomeAdapter(pages)

        binding.dots.tabIconTint = null
        TabLayoutMediator(binding.dots, binding.viewPager) { tab, _ ->
            tab.setIcon(R.drawable.dot_selector)
        }.attach()


        pageCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == pages.lastIndex && !autoNavDone) {
                    autoNavDone = true
                    binding.viewPager.postDelayed({
                        startActivity(Intent(this@WelcomeActivity, UbicacionActivity::class.java))
                        finish()
                    }, 900)
                }
            }
        }
        binding.viewPager.registerOnPageChangeCallback(pageCallback)


        binding.OmitirTxtView.setOnClickListener {
            startActivity(Intent(this, UbicacionActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        if (::pageCallback.isInitialized) {
            binding.viewPager.unregisterOnPageChangeCallback(pageCallback)
        }
        super.onDestroy()
    }
}


