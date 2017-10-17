package vn.asiantech.way.ui.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.android.synthetic.main.activity_share_location.*
import kotlinx.android.synthetic.main.bottom_button_card_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.remote.APIUtil
import vn.asiantech.way.data.model.LocationResponse
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.util.LocationUtil

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class ShareLocationActivity : BaseActivity(), OnMapReadyCallback {
    private val service = APIUtil.getService()
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fgMap) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        initializeUIViews()
        onClickButtonSearchLocation()
        initBottomButtonCard(true)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        // Add a marker in Sydney and move the camera
        val myLocation = LocationUtil(this).getCurrentLocation()
        val myLaLng = myLocation?.latitude?.let { LatLng(it, myLocation.longitude) }
        if (myLaLng != null) {
            val cameraMove = CameraUpdateFactory.newLatLngZoom(
                    myLaLng, 16f)
            googleMap?.addMarker(myLaLng?.let {
                MarkerOptions().position(it)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_source_place_marker))
                        .anchor(0.5f, 0.5f)
            })
            googleMap?.animateCamera(cameraMove)
            getLocationAddress("${myLaLng?.latitude},${myLaLng?.longitude}")
        }
    }


    private fun initializeUIViews() {
        bottomButtonCard?.buttonListener = object : BottomButtonCard.ButtonListener {
            override fun onCloseButtonClick() {
                // No-op
            }

            override fun onActionButtonClick() {
                shareLocation()
            }

            override fun onCopyButtonClick() {
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                        ClipData.newPlainText("tracking_url", bottomButtonCard.tvURL.text)
            }

        }
    }

    //TODO: will change code logic in future
    private fun initBottomButtonCard(show: Boolean) {
        bottomButtonCard?.hideCloseButton()
        bottomButtonCard?.setDescriptionText("")
        bottomButtonCard?.actionType = BottomButtonCard.ActionType.SHARE_TRACKING_URL
        bottomButtonCard?.showTrackingURLLayout()
        bottomButtonCard?.setTitleText(getString(R.string.textview_text_test_title_text))
        bottomButtonCard?.setDescriptionText(getString(R.string.textview_text_test_description_text))
        bottomButtonCard?.setShareButtonText(getString(R.string.textview_text_start_sharing))
        bottomButtonCard?.showActionButton()
        bottomButtonCard?.showTitle()
        if (show) {
            bottomButtonCard?.showBottomCardLayout()
        }
    }

    private fun shareLocation() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        val message = "My Location is ${bottomButtonCard.tvURL.text}"
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message)
        startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), 200)
    }

    //todo: will update code with base API
    private fun getLocationAddress(latLng: String) {
        service?.getAddressLocation(latLng)?.enqueue(object : Callback<LocationResponse> {
            override fun onResponse(call: Call<LocationResponse>?, response: Response<LocationResponse>?) {
                response?.isSuccessful?.let {
                    if (!it) {
                        return
                    }
                }
                if ("INVALID_REQUEST" == response?.body()?.status) {
                    return
                }
                val locations = response?.body()?.results
                val address = locations?.get(0)
                tvAddress.text = address?.address
                tvAddress.setTextColor(Color.BLACK)
            }

            override fun onFailure(call: Call<LocationResponse>?, t: Throwable?) {
            }

        })
    }

    private fun onClickButtonSearchLocation() {
        rlSearchLocation.setOnClickListener {
            //TODO:Update event onCLick for Search location
        }
    }
}
