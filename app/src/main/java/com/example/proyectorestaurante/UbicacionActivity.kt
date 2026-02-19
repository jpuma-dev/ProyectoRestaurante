package com.example.proyectorestaurante

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class UbicacionActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // OSMDroid config
        val conf = Configuration.getInstance()
        conf.userAgentValue = packageName
        conf.load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // MapView
        mapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)

        // Punto fijo (restaurante)
        val punto = GeoPoint(-16.3989, -71.5350)

        mapView.controller.setZoom(18.0)
        mapView.controller.setCenter(punto)

        val marker = Marker(mapView)
        marker.position = punto
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Restaurante"
        mapView.overlays.add(marker)

        // Back
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        // Comencemos -> ConfUbiActivity
        findViewById<MaterialButton>(R.id.btnComencemos).setOnClickListener {
            startActivity(Intent(this, ConfUbiActivity::class.java))
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


