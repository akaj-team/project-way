package vn.asiantech.way.ui.confirm_location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_confirm_location.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment


/**
 * Fragment confirm location
 * Created by haingoq on 10/10/2017.
 */
class ConfirmLocationFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener {
    var mGoogleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.fragment_confirm_location, container, false)
        val mapFragment = fragmentMap as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return view
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        mGoogleMap?.setOnCameraMoveListener(this)
    }

    override fun onCameraMove() {
        val l: LatLng? = mGoogleMap?.cameraPosition?.target
        Log.d("xxx", "$l")
    }
}
