package vn.asiantech.way.ui.arrived

import android.app.FragmentManager
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 26/09/2017.
 */
class ArrivedActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrived)
       val dialog=DialogShowArrivedInfor()
        val fragmentManager =fragmentManager as? FragmentManager
        dialog.show(fragmentManager,"Dialog Fragment")
        val map:SupportMapFragment?= supportFragmentManager.findFragmentById(R.id.fragmentGoogleMap) as? SupportMapFragment
        map?.getMapAsync(object:OnMapReadyCallback{
            override fun onMapReady(p0: GoogleMap?) {

            }

        })
    }
}
