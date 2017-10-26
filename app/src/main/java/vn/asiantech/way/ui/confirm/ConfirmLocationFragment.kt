package vn.asiantech.way.ui.confirm

import android.annotation.SuppressLint
import android.content.Intent
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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_confirm_location.*
import kotlinx.android.synthetic.main.view_confirm_location_layout.*
import kotlinx.android.synthetic.main.view_confirm_location_layout.view.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.ui.search.SearchLocationActivity
import vn.asiantech.way.utils.LocationUtil
import java.util.*

/**
 * Fragment confirm location
 * Created by haingoq on 10/10/2017.
 */
class ConfirmLocationFragment : BaseFragment(), OnMapReadyCallback,
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnCameraIdleListener, View.OnClickListener {

    private var mGoogleMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mLatLng: LatLng? = null
    private var mDestinationName: String? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mMarker: Marker? = null
    private var mGroundOverlay: GroundOverlay? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_confirm_location, container, false)
        initMap()
        initGoogleApiClient()
        initLocationRequest()
        initListener(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient?.disconnect()
    }

    private fun initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build()
        }
    }

    private fun initLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = 500
        mLocationRequest?.fastestInterval = 500
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onConnectionSuspended(p0: Int) {
        //No op
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
        if (mMarker != null && location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            mMarker?.position = latLng
            mGroundOverlay?.position = latLng
        }
    }

    override fun onCameraIdle() {
        mLatLng = mGoogleMap?.cameraPosition?.target
        getLocationName(mLatLng)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgEdit -> {
                startActivity(Intent(activity, SearchLocationActivity::class.java))
            }
            R.id.btnConfirm -> {
                addDestinationMarker(mLatLng)
                //TODO handle share location
            }
        }
    }

    private fun initListener(view: View?) {
        view?.imgEdit?.setOnClickListener(this)
        view?.btnConfirm?.setOnClickListener(this)
    }

    private fun initMap() {
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragmentConfirmMap) as SupportMapFragment?
        mMapFragment?.getMapAsync(this)
    }

    private fun getLocationName(latLng: LatLng?) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latLng!!.latitude, latLng.longitude, 1)
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

    private fun addDestinationMarker(latLng: LatLng?) {
        mGoogleMap?.addMarker(MarkerOptions()
                .position(latLng!!)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                .title(mDestinationName)
                .anchor(0.5f, 0.5f))
                ?.showInfoWindow()
        imgPickLocation.visibility = View.INVISIBLE
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
    }

    private fun drawCurrentMaker(location: Location) {
        val currentLocation = LatLng(location.latitude, location.longitude)
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            mMarker = mGoogleMap?.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
                    .title(getString(R.string.current_location))
                    .anchor(0.5f, 0.5f))
            addPulseRing(currentLocation)
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
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
        mGroundOverlay = mGoogleMap?.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, 500f)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
        val groundAnimation = RadiusAnimation(mGroundOverlay)
        groundAnimation.repeatCount = Animation.INFINITE
        groundAnimation.duration = 2000
        mMapFragment?.view?.startAnimation(groundAnimation)
    }
}
