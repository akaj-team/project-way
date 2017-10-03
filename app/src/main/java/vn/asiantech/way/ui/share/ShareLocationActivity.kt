package vn.asiantech.way.ui.share

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackEventCallback
import com.hypertrack.lib.internal.transmitter.models.HyperTrackEvent
import com.hypertrack.lib.models.ErrorResponse
import kotlinx.android.synthetic.main.activity_share_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.api.APIUtil
import vn.asiantech.way.api.LocationResponse
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.utils.GPSUtil


/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class ShareLocationActivity : BaseActivity(), OnMapReadyCallback {
    private val service = APIUtil.getService()
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fgMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initializeUIViews()
        onClickButtonSearchLoaction()
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        // Add a marker in Sydney and move the camera
        val myLocation = GPSUtil(this).getCurrentLocation()
        val myLaLng = LatLng(myLocation.latitude, myLocation.longitude)
        val cameraMove = CameraUpdateFactory.newLatLngZoom(
                myLaLng, 16f)
        googleMap?.addMarker(MarkerOptions().position(myLaLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_source_place_marker))
                .anchor(0.5f, 0.5f))
        googleMap?.animateCamera(cameraMove)
        getLocationAddress("${myLaLng.latitude},${myLaLng.longitude}")

    }


    private fun initializeUIViews() {
        bottomButtonCard?.buttonListener = object : BottomButtonCard.ButtonListener {
            override fun OnCloseButtonClick() {
            }

            override fun OnActionButtonClick() {
                initBottomButtonCard(true)
                bottomButtonCard.startProgress()
            }

            override fun OnCopyButtonClick() {
            }

        }
    }

    private fun initBottomButtonCard(show: Boolean) {
        bottomButtonCard?.hideCloseButton()
        bottomButtonCard?.setDescriptionText("")
        bottomButtonCard?.actionType = BottomButtonCard.ActionType.START_TRACKING
        bottomButtonCard?.hideTrackingURLLayout()
        bottomButtonCard?.setTitleText(getString(R.string.textview_text_look_good))
        bottomButtonCard?.setShareButtonText(getString(R.string.textview_text_start_sharing))
        bottomButtonCard?.showActionButton()
        bottomButtonCard?.showTitle()
        if (show) {
            bottomButtonCard?.showBottomCardLayout()
        }
    }

    //todo: will update code with base API
    private fun getLocationAddress(latLng: String) {
        service?.getAddressLocation(latLng)?.enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>?, response: Response<LocationResponse>?) {
                if (!response!!.isSuccessful) {
                    return
                }
                if ("INVALID_REQUEST".equals(response.body()?.status)) {
                    return
                }
                val locations = response.body()?.results
                val address = locations?.get(0)
                tvAddress.text = address?.address
                tvAddress.setTextColor(Color.BLACK)
            }

            override fun onFailure(call: Call<LocationResponse>?, t: Throwable?) {
                Log.d("TTTTTT", "fail")
            }

        })
    }

    private fun onClickButtonSearchLoaction() {
        rlSearchLocation.setOnClickListener {
            //TODO:Update event onCLick for Search location
        }
    }
}