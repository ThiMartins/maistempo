package dev.tantto.maistempo.Telas

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dev.tantto.maistempo.Chaves.Chaves
import dev.tantto.maistempo.Modelos.Local
import dev.tantto.maistempo.R

class TelaMapa : AppCompatActivity(), OnMapReadyCallback {

    private val RequisicaoPermissaoFINE = 2
    private val RequisicaoPermissaoLOCATION = 3

    private var MapaGoogle:GoogleMap? = null

    private var ListaMaps = mutableListOf<Local>(
        Local(-23.507595, -47.483918, "Teste"),
        Local(-23.506569, -47.488340, "Teste 2")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        val FragmentTela = supportFragmentManager.findFragmentById(R.id.TelaMapa) as SupportMapFragment
        FragmentTela.getMapAsync(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onMapReady(p0: GoogleMap?) {
        MapaGoogle = p0
        p0?.isBuildingsEnabled = true
        p0?.isTrafficEnabled = true
        p0?.isIndoorEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                p0?.isMyLocationEnabled = true
            }
            else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), RequisicaoPermissaoLOCATION)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RequisicaoPermissaoFINE)
            }
        }
        if(intent.hasExtra(Chaves.CHAVE_MARKES.valor)){

        }

        adicionarMaker(p0)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RequisicaoPermissaoFINE -> {
                @SuppressLint("MissingPermission")
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    MapaGoogle?.isMyLocationEnabled = true
                }
            }
        }
    }

    private fun adicionarMaker(mapa:GoogleMap?){
        val Builder = LatLngBounds.builder()
        for(Item in ListaMaps){
            val Coordenadas = LatLng(Item.Longitude, Item.Latitude)
            mapa?.addMarker(MarkerOptions().position(Coordenadas).title(Item.Nome))
            Builder.include(Coordenadas)
        }

        val Bounds = Builder.build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.10).toInt()
        mapa?.animateCamera(CameraUpdateFactory.newLatLngBounds(Bounds, width, height, padding))
    }

}