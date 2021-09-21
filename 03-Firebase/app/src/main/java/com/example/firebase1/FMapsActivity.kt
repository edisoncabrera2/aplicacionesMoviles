package com.example.firebase1

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions

class FMapsActivity : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    var permisos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fmaps)
        solicitarPermisos()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{ googleMap->
            if(googleMap != null){
                mapa = googleMap
                establecerConfuguracionMapa()
                val quicentro = LatLng(-0.17617322571324992, -78.47921292858925)
                val titulo = "Quicentro"
                val zoom = 17f
                anadirMarcador(quicentro,titulo)
                moverCamaraConZoom(quicentro,zoom)
                // LINEA
                val poliLineaUno = googleMap
                    .addPolyline (
                        PolylineOptions ()
                            .clickable (true)
                            .add(
                                LatLng ( -0.1759187040647396 , -78.48506472421384 ),
                                LatLng ( -0.17632468492901104 , -78.48265589308046 ),
                                LatLng ( -0.17746143130181483 , -78.4770533307815 )
                            )
                    )
                poliLineaUno.tag =  " linea-1 "  // <- ID

                // POLIGONO
                val poligonoUno = googleMap
                    .addPolygon (
                        PolygonOptions ()
                            .clickable (true)
                            .add(
                                LatLng ( -0.1771546902239471 , -78.48344981495214 ),
                                LatLng ( -0.17968981486125768 , -78.48269198043828 ),
                                LatLng ( -0.17710958124147777 , -78.48142892291516 )
                            )
                    )
                poligonoUno.fillColor =  - 0xc771c4
                poligonoUno.tag =  " poligono-2 "  // <- ID
                escucharListener()
            }
        }
        val btnCarolina = findViewById<Button>(R.id.btn_ir_carolina)
        btnCarolina.setOnClickListener{
            val carolina = LatLng(-0.1819882619985097, -78.48386118871714)
            val zoom = 17f
            moverCamaraConZoom(carolina,zoom)
            escucharListener()
        }
    }


    fun moverCamaraConZoom(latLng: LatLng, zoom: Float = 10f) {
        mapa.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        )
    }

    fun anadirMarcador(latLng: LatLng, title: String) {
        mapa.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
        )
    }


    fun solicitarPermisos(){
        val contexto = this.applicationContext
        val permisosFineLocation = ContextCompat
            .checkSelfPermission(
                contexto,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
        if(tienePermisos){
            permisos = true
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(  // Arreglo Permisos
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1  // Codigo de peticion de los permisos
            )
        }
    }


    fun establecerConfuguracionMapa() {
        val contexto = this.applicationContext
        with(mapa){
            val permisosFineLocation = ContextCompat
                .checkSelfPermission(
                    contexto,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
            if(tienePermisos){
                mapa.isMyLocationEnabled = true // no tiene aun permisos
            }
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true // no tenemos aun permisos
        }

    }

    fun escucharListener(){
        mapa.setOnPolygonClickListener {
            Log.i("mapa", "setOnPolygonClickListener ${it}")
        }
        mapa.setOnPolylineClickListener {
            Log.i("mapa", "setOnPolylineClickListener${it}")
        }
        mapa.setOnMarkerClickListener{
            Log.i("mapa", "setOnMarkerClickListener ${it}")
            return@setOnMarkerClickListener true
        }
        mapa
            .setOnCameraMoveListener {
            Log.i("mapa", "setOnCameraMoveListener")
        }
        mapa
            .setOnCameraMoveStartedListener {
            Log.i("mapa", "setOnCameraMoveStartedListener ${it}")
        }
        mapa
            .setOnCameraIdleListener{
            Log.i("mapa", "setOnCameraIdleListener")
        }
    }
}
