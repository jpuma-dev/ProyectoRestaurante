package com.example.proyectorestaurante

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class ConfUbiActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val conf = Configuration.getInstance()
        conf.userAgentValue = packageName
        conf.load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        enableEdgeToEdge()
        setContentView(R.layout.activity_conf_ubi)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        mapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)


        val punto = GeoPoint(-16.3989, -71.5350)


        mapView.controller.setZoom(18.0)
        mapView.controller.setCenter(punto)


        val marker = Marker(mapView)
        marker.position = punto
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Restaurante"
        mapView.overlays.add(marker)


        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        val etDireccion = findViewById<EditText>(R.id.etDireccion)


        findViewById<ImageView>(R.id.btnGps).setOnClickListener {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        }


        findViewById<MaterialButton>(R.id.btnComencemos).setOnClickListener {

            val direccion = etDireccion.text.toString().trim()


            if (direccion.isEmpty()) {
                etDireccion.error = "Ingresa tu dirección"
                return@setOnClickListener
            }



            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            prefs.edit()
                .putString("direccion", direccion)
                .apply()


            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
