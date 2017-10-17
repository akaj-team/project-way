package vn.asiantech.way.ui.confirm_location

import android.animation.ValueAnimator
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arsy.maps_library.MapRipple
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.view_confirm_location_layout.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.util.LocationUtil
import java.util.*

/**
 * Fragment confirm location
 * Created by haingoq on 10/10/2017.
 */
class ConfirmLocationFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    var mGoogleMap: GoogleMap? = null
    var mView: View? = null
    private val pulseCount = 4
    private val animationDuration = (pulseCount + 1) * 1000
    private var circles = Array<Circle?>(pulseCount, { null })

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_confirm_location, container, false)
        initGoogleMap()
        return mView
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        val currentLocation: Location? = LocationUtil(context).getCurrentLocation()
        if (currentLocation != null) {
            drawMaker(currentLocation)
        }
        mGoogleMap?.setOnCameraIdleListener(this)
    }

    override fun onCameraIdle() {
        val latLng: LatLng? = mGoogleMap?.cameraPosition?.target
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
        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            tvLocation.text = address.getAddressLine(0)
        } else {
            tvLocation.text = null
        }
    }

    private fun drawMaker(location: Location) {
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap?.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
                    .title(getString(R.string.current_location)))
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
            addMarkerAnimation(currentLocation)
//            val from = 0
//            val to = 100
//            val fraction = 255 / to
//
//            addPulseAnimator(currentLocation, from, to, fraction, 1)
        }
    }

    private fun addMarkerAnimation(latLng: LatLng) {
        val mapRipple = MapRipple(mGoogleMap, latLng, context)
        mapRipple.withNumberOfRipples(1)
        mapRipple.withRippleDuration(2000)
        mapRipple.withFillColor(Color.BLUE)
        mapRipple.withDistance(100.0)
        mapRipple.startRippleMapAnimation()
    }

    private fun addPulseAnimator(latLng: LatLng, from: Int, to: Int, colorFraction: Int, currentPosition: Int) {
        val valueAnimator = ValueAnimator.ofInt(from, to)
        valueAnimator.duration = animationDuration.toLong()
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.RESTART
        valueAnimator.startDelay = currentPosition * 1000L
        valueAnimator.addUpdateListener { valueAnimator ->

            val radius = valueAnimator.animatedValue as Int

            circles[currentPosition]?.let { circle ->
                circle.center = latLng
                circle.radius = radius.toDouble()
                circle.fillColor = Color.argb((to - radius) * colorFraction, 48, 118, 254)
                circle.strokeWidth = 0f

            } ?: run {
                circles[currentPosition] = mGoogleMap?.addCircle(CircleOptions()
                        .center(latLng)
                        .radius(radius.toDouble())
                        .fillColor(Color.argb((to - radius) * colorFraction, 48, 118, 254))
                        .strokeWidth(0f))
            }
        }
        valueAnimator.start()
    }
}
