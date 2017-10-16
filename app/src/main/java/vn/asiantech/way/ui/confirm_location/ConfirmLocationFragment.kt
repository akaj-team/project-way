package vn.asiantech.way.ui.confirm_location

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment
import java.util.*


/**
 * Fragment confirm location
 * Created by haingoq on 10/10/2017.
 */
class ConfirmLocationFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener {
    var mGoogleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.fragment_confirm_location, container, false)
        initGoogleMap()
        return view
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        mGoogleMap?.setOnCameraMoveListener(this)
    }

    override fun onCameraMove() {
        val latLng: LatLng? = mGoogleMap?.cameraPosition?.target
        Log.d("xxx", "$latLng")
        getLocationName(latLng!!)
    }

    private fun initGoogleMap() {
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.fragmentConfirmMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLocationName(latLng: LatLng) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        val address: Address = addresses[0]
        Log.d("xxx", "" + addresses.size)
    }
}
