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

        // Configuración de OSMDroid:
        // Se establece un UserAgent válido para evitar bloqueos en la descarga de tiles
        // y se cargan preferencias internas del motor de mapas.
        val conf = Configuration.getInstance()
        conf.userAgentValue = packageName
        conf.load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))

        enableEdgeToEdge()
        setContentView(R.layout.activity_conf_ubi)

        // Ajuste automático de márgenes para barras del sistema (status/navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización del MapView
        mapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true) // habilita zoom y gestos

        // Ubicación fija del restaurante (coordenadas geográficas)
        val punto = GeoPoint(-16.3989, -71.5350)

        // Configuración de cámara del mapa
        mapView.controller.setZoom(18.0)
        mapView.controller.setCenter(punto)

        // Marcador del restaurante en el mapa
        val marker = Marker(mapView)
        marker.position = punto
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Restaurante"
        mapView.overlays.add(marker)

        // Botón Atrás → Finaliza la actividad actual
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        val etDireccion = findViewById<EditText>(R.id.etDireccion)

        // Botón GPS (simulación / pendiente de implementación real)
        findViewById<ImageView>(R.id.btnGps).setOnClickListener {
            Toast.makeText(this, "Luego conectamos GPS", Toast.LENGTH_SHORT).show()
        }

        // Botón Comencemos → Validar dirección y navegar
        findViewById<MaterialButton>(R.id.btnComencemos).setOnClickListener {

            val direccion = etDireccion.text.toString().trim()

            // Validación básica de entrada
            if (direccion.isEmpty()) {
                etDireccion.error = "Ingresa tu dirección"
                return@setOnClickListener
            }

            // Navegación entre pantallas:
            // Se envía la dirección mediante Intent (memoria temporal entre Activities)
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("direccion", direccion)

            startActivity(i)
            finish() // evita volver atrás
        }
    }

    // Ciclo de vida del MapView (requerido por OSMDroid)
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
