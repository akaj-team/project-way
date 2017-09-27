package vn.asiantech.way.ui.share

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import com.google.android.gms.maps.CameraUpdate
import kotlinx.android.synthetic.main.activity_share_location.*
import vn.asiantech.way.ui.splash.SplashActivity


/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class ShareLocationActivity : BaseActivity(), OnMapReadyCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fgMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap?) {
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(16.0472484, 108.1716005)
        val location = CameraUpdateFactory.newLatLngZoom(
                sydney, 15f)
        p0?.animateCamera(location)
    }

}