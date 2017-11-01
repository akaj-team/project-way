package vn.asiantech.way.ui.confirm

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import kotlinx.android.synthetic.main.fragment_confirm_location.*
import kotlinx.android.synthetic.main.view_confirm_location_layout.view.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.ui.search.SearchLocationActivity
import vn.asiantech.way.utils.LocationUtil

/**
 * Fragment confirm location
 * Created by haingoq on 10/10/2017.
 */
class ConfirmLocationFragment : BaseFragment(), OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnCameraIdleListener, View.OnClickListener {

    companion object {
        private const val INTERVAL = 500L
    }

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mDestinationName: String
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest
    private var mLatLng: LatLng? = null
    private var mMarker: Marker? = null
    private var mGroundOverlay: GroundOverlay? = null
    private lateinit var mLocationAsyncTask: AsyncTask<LatLng, Void, String>

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
        mGoogleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.disconnect()
        if(!mLocationAsyncTask.isCancelled) {
            mLocationAsyncTask.cancel(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if(!mLocationAsyncTask.isCancelled) {
            mLocationAsyncTask.cancel(true)
        }
    }

    private fun initGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun initLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = INTERVAL
    }

    override fun onConnected(p0: Bundle?) {
        if (!HyperTrack.checkLocationPermission(activity)) {
            HyperTrack.requestPermissions(activity)
        }

        if (!HyperTrack.checkLocationServices(activity)) {
            HyperTrack.requestLocationServices(activity)
        }

        if (HyperTrackUtils.isInternetConnected(activity)) {
            if (HyperTrackUtils.isLocationEnabled(activity)) {
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        //No op
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            mGoogleMap = map
            mGoogleMap.setOnCameraIdleListener(this)
        }
        //Get current location
        val currentLocation: Location? = LocationUtil(context).getCurrentLocation()
        if (currentLocation != null) {
            drawCurrentMaker(currentLocation)
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (mMarker != null && mGroundOverlay != null && location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            mMarker?.position = latLng
            mGroundOverlay?.position = latLng
        }
    }

    override fun onCameraIdle() {
        mLatLng = mGoogleMap.cameraPosition.target
        mLocationAsyncTask = LocationNameAsyncTask(this).execute(mLatLng)
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
        mMapFragment = childFragmentManager.findFragmentById(R.id.fragmentConfirmMap) as SupportMapFragment
        mMapFragment.getMapAsync(this)
    }

    /**
     * Set destination name
     */
    fun setDestinationName(name: String) {
        mDestinationName = name
    }

    private fun addDestinationMarker(latLng: LatLng?) {
        mGoogleMap.addMarker(MarkerOptions()
                .position(latLng!!)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                .title(mDestinationName)
                .anchor(0.5f, 0.5f))
                ?.showInfoWindow()
        imgPickLocation.visibility = View.INVISIBLE
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }

    private fun drawCurrentMaker(location: Location) {
        val currentLocation = LatLng(location.latitude, location.longitude)
        mGoogleMap.clear()
        mMarker = mGoogleMap.addMarker(MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
                .title(getString(R.string.current_location))
                .anchor(0.5f, 0.5f))
        addPulseRing(currentLocation)
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
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
        mGroundOverlay = mGoogleMap.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, 500f)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
        val groundAnimation = RadiusAnimation(mGroundOverlay)
        groundAnimation.repeatCount = Animation.INFINITE
        groundAnimation.duration = 2000
        mMapFragment.view?.startAnimation(groundAnimation)
    }
}
