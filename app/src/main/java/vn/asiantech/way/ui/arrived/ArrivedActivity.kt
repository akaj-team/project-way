package vn.asiantech.way.ui.arrived

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrackMapFragment
import kotlinx.android.synthetic.main.activity_arrived.*
import kotlinx.android.synthetic.main.show_detail_arrived.*
import vn.asiantech.way.R
import vn.asiantech.way.models.Arrived
import vn.asiantech.way.ui.base.BaseActivity

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 26/09/2017.
 */
class ArrivedActivity : BaseActivity() {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mDestinationLocation: Location
    private lateinit var mArrived: Arrived
    private lateinit var mPoints: MutableList<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrived)
        val map: HyperTrackMapFragment? = supportFragmentManager.findFragmentById(R.id.fragmentGoogleMap) as? HyperTrackMapFragment
        setArrivedDetail()
        configFirst()
        if (checkDestination(LatLng(mArrived.segments[mArrived.segments.size - 1].endLocation.latitude, mArrived.segments[mArrived.segments.size - 1].endLocation.longitude))) {
            arrivedFinish(mArrived.time!!, mArrived.distance!!, mArrived.averageSpeed!!)
            map?.addCustomMarker(setMarkerOption(R.drawable.ic_ht_source_place_marker, LatLng(mArrived.segments[0].startLocation.latitude, mArrived.segments[0].startLocation.longitude)))
            map?.addCustomMarker(setMarkerOption(R.drawable.ic_ht_expected_place_marker, LatLng(mArrived.segments[mArrived.segments.size - 1].endLocation.latitude, mArrived.segments[mArrived.segments.size - 1].endLocation.longitude)))
        }
        btnShowSummary.setOnClickListener {
            showDialog(mArrived.time!!, mArrived.distance!!, mArrived.averageSpeed!!)
        }
    }

    private fun setArrivedDetail() {
        //TODO("set detail for m")
    }

    private fun configFirst() {
        mArrived.segments.forEach {
            mPoints.add(LatLng(it.startLocation.latitude, it.startLocation.longitude))
            mPoints.add(LatLng(it.endLocation.latitude, it.endLocation.longitude))
        }
    }

    private fun checkDestination(latlng: LatLng): Boolean {
        return if (latlng.latitude == mDestinationLocation.latitude && latlng.longitude == mDestinationLocation.longitude) true else false
    }

    private fun arrivedFinish(time: String, distance: String, averageSpeed: String) {
        btnShowSummary.visibility = View.VISIBLE
        progressBarCircular.progress = 100
        tvTimeTotalArrived.text = time
        tvDistanceArrived.text = distance
        tvAverageSpeedDialog.text = averageSpeed
        drawLine()
    }

    private fun drawLine() {
        val polyPointOption = PolylineOptions()
        polyPointOption.addAll(mPoints)
        polyPointOption.color(Color.BLACK)
        polyPointOption.width(2f)
        mGoogleMap.addPolyline(polyPointOption)
    }

    private fun showDialog(time: String, distance: String, averageSpeed: String) {
        val dialog = DialogShowArrivedInfor.newInstance(time, distance, averageSpeed)
        val fragmentManager = supportFragmentManager as? FragmentManager
        dialog.show(fragmentManager, "Dialog Fragment")
    }

    private fun setMarkerOption(resource: Int, position: LatLng): MarkerOptions = MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(resource))
}
