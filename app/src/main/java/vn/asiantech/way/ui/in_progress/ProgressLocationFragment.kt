package vn.asiantech.way.ui.in_progress

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hypertrack.lib.HyperTrack.stopTracking
import kotlinx.android.synthetic.main.fragment_progress_location.*
import vn.asiantech.way.R


/**
 * A simple [Fragment] subclass.
 */
class ProgressLocationFragment : Fragment(), OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleMap: GoogleMap? = null

    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private var mMapView: View? = null

    private val mCurrentBatteryReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(p0: Context?, p1: Intent?) {
            val level = p1?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            tvBattery.text = "${level.toString()}%"
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_progress_location, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkGPS()
        initView()
        activity.registerReceiver(mCurrentBatteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        addEvents()
    }

    private fun initView() {
        val mapFragment = activity.fragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        mMapView = mapFragment.view
        mapFragment.getMapAsync(this)
    }

    private fun addEvents() {
        rlCollapse.setOnClickListener {
            if (rlExpandedInfo.visibility == View.GONE) {
                rlExpandedInfo.visibility = View.VISIBLE
                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
            } else {
                rlExpandedInfo.visibility = View.GONE
                imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
            }
        }
        //TODO Action ripple here!
        rippleTrackingToggle.setOnRippleCompleteListener {
            if (rippleTrackingToggle.tag == "stop") {
                stopTracking()
            } else if (rippleTrackingToggle.tag == "summary") {

            }
        }
        rippleShareLink.setOnClickListener { Toast.makeText(context, "SHARE SHARE SHARE", Toast.LENGTH_SHORT).show() }
    }

    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap = p0
        mGoogleMap?.addMarker(MarkerOptions()
                .title("A Place!")
                .position(LatLng(16.082709, 108.236512))
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker_car))
        )
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(16.074812, 108.233132), 14f))
    }

    // check GPS
    private fun checkGPS() {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Turn on GPS")
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        //                        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        ActivityCompat.requestPermissions(activity,
                                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                                1)
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
            builder.create().show()
        }
    }

    override fun onLocationChanged(p0: Location?) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
