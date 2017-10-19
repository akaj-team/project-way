package vn.asiantech.way.ui.confirm_location

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_confirm_location.*
import kotlinx.android.synthetic.main.view_confirm_location_layout.*
import kotlinx.android.synthetic.main.view_confirm_location_layout.view.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.utils.LocationUtil
import java.util.*

/**
 * Fragment confirm location
 * Created by haingoq on 10/10/2017.
 */
class ConfirmLocationFragment : BaseFragment(), OnMapReadyCallback,
        LocationSource.OnLocationChangedListener,
        GoogleMap.OnCameraIdleListener, View.OnClickListener {
    var mGoogleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var mLatLng: LatLng? = null
    private var mDestinationName: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_confirm_location, container, false)
        initMap()
        initListener(view)
        return view
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        //Get current location
        val currentLocation: Location? = LocationUtil(context).getCurrentLocation()
        if (currentLocation != null) {
            drawCurrentMaker(currentLocation)
        }
        mGoogleMap?.setOnCameraIdleListener(this)
    }

    override fun onLocationChanged(location: Location?) {
        drawCurrentMaker(location!!)
    }

    override fun onCameraIdle() {
        mLatLng = mGoogleMap?.cameraPosition?.target
        getLocationName()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgEdit -> {
                //TODO intent to search
            }
            R.id.btnConfirm -> {
                addDestinationMarker()
                //TODO handle share location
            }
        }
    }

    private fun initListener(view: View) {
        view.imgEdit.setOnClickListener(this)
        view.btnConfirm.setOnClickListener(this)
    }

    private fun initMap() {
        mapFragment = childFragmentManager
                .findFragmentById(R.id.fragmentConfirmMap) as SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun getLocationName() {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(mLatLng!!.latitude, mLatLng!!.longitude, 1)
        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            tvLocation.text = address.getAddressLine(0)
            if (!address.subThoroughfare.isNullOrEmpty()) {
                mDestinationName = address.subThoroughfare.plus(" ").plus(address.thoroughfare)
            } else {
                mDestinationName = address.thoroughfare
            }
        } else {
            tvLocation.text = null
        }
    }

    private fun addDestinationMarker() {
        mGoogleMap?.addMarker(MarkerOptions()
                .position(mLatLng!!)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                .title(mDestinationName)
                .anchor(0.5f, 0.5f))
                ?.showInfoWindow()
        imgPickLocation.visibility = View.INVISIBLE
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16f))
    }

    private fun drawCurrentMaker(location: Location) {
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap?.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
                    .title(getString(R.string.current_location))
                    .anchor(0.5f, 0.5f))
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
            addPulseRing(currentLocation)
        }
    }

    private fun addPulseRing(latLng: LatLng) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setSize(500, 500)
        drawable.setColor(ContextCompat.getColor(context, R.color.pulse_color))

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        val groundOverlay = mGoogleMap?.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, 500f)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
        val groundAnimation = RadiusAnimation(groundOverlay!!)
        groundAnimation.repeatCount = Animation.INFINITE
        groundAnimation.duration = 2000
        mapFragment?.view?.startAnimation(groundAnimation)
    }
}
