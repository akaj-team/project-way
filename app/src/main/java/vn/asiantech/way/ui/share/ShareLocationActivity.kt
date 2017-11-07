package vn.asiantech.way.ui.share

import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
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
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.*
import kotlinx.android.synthetic.main.activity_share_location.*
import kotlinx.android.synthetic.main.bottom_button_card_view.*
import kotlinx.android.synthetic.main.bottom_button_card_view.view.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.search.MyLocation
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.confirm.LocationNameAsyncTask
import vn.asiantech.way.ui.custom.BottomButtonCard
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.ui.search.SearchLocationActivity
import vn.asiantech.way.utils.AppConstants
import vn.asiantech.way.utils.LocationUtil
import java.lang.ref.WeakReference
import java.util.*

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class ShareLocationActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, LocationListener, GoogleApiClient.ConnectionCallbacks {

    companion object {
        private const val INTERVAL = 500L
    }

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest
    private var mDestinationName: String? = null
    private var mLatLng: LatLng? = null
    private var mMyLocation: MyLocation? = null
    private var mAction: String? = null
    private var mIsConfirm: Boolean = false
    private var mMarker: Marker? = null
    private var mGroundOverlay: GroundOverlay? = null
    private lateinit var mLocationAsyncTask: AsyncTask<LatLng, Void, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        if (intent.extras != null) {
            mMyLocation = intent.getParcelableExtra(AppConstants.KEY_LOCATION)
            mAction = intent.getStringExtra(AppConstants.KEY_CONFIRM)
        }
        initMap()
        initializeUIViews()
        initGoogleApiClient()
        initLocationRequest()
        onClickButtonSearchLocation()
        initBottomButtonCard(true, mAction)
    }


    private fun initMap() {
        mMapFragment = supportFragmentManager.findFragmentById(R.id.fgMap) as SupportMapFragment
        mMapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            mGoogleMap = map
            mGoogleMap.setOnCameraIdleListener(this)
        }
        val currentLocation = LocationUtil(this).getCurrentLocation()
        if (currentLocation != null) {
            drawCurrentMaker(currentLocation)
        }
        mGoogleMap.setOnCameraIdleListener(this)
        val lat = mMyLocation?.geometry?.location?.lat
        val lng = mMyLocation?.geometry?.location?.lng
        if (lat != null && lng != null) {
            mLatLng = LatLng(lat, lng)
            addDestinationMarker(mLatLng)
        }
        if (mAction == AppConstants.KEY_CURRENT_LOCATION) {
            mLatLng = currentLocation?.latitude?.let { LatLng(it, currentLocation.longitude) }
            addDestinationMarker(mLatLng)
            getLocationName(mLatLng)
            mIsConfirm = true
        }
    }

    override fun onLocationChanged(location: Location) {
        if (mMarker != null && mGroundOverlay != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            mMarker?.position = latLng
            mGroundOverlay?.position = latLng
        }
    }

    override fun onCameraIdle() {
        mLatLng = mGoogleMap.cameraPosition?.target
        if (!mIsConfirm) {
            if (mMyLocation?.geometry?.location == null) {
                mLocationAsyncTask = LocationNameAsyncTask(WeakReference(this)).execute(mLatLng)
            } else {
                val lat = mMyLocation?.geometry?.location?.lat
                val lng = mMyLocation?.geometry?.location?.lng
                if (lat != null && lng != null) {
                    mLatLng = LatLng(lat, lng)
                    mLocationAsyncTask = LocationNameAsyncTask(WeakReference(this)).execute(mLatLng)
                }
            }
        }
    }

    private fun initializeUIViews() {
        bottomButtonCard?.buttonListener = object : BottomButtonCard.ButtonListener {
            override fun onCloseButtonClick() {
                rlBottomCard.visibility = View.GONE
            }

            override fun onActionButtonClick() {
                when (mAction) {
                    AppConstants.KEY_CONFIRM -> {
                        mAction = AppConstants.KEY_SHARING
                        initBottomButtonCard(true, mAction)
                        addDestinationMarker(mLatLng)
                        mIsConfirm = true
                    }
                // Click sharing
                    AppConstants.KEY_SHARING, AppConstants.KEY_CURRENT_LOCATION -> {
                        mAction = AppConstants.KEY_START_SHARING
                        bottomButtonCard.startProgress()
                        getTrackingURL()
                    }
                    else -> shareLocation()
                }
            }

            override fun onCopyButtonClick() {
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                        ClipData.newPlainText("tracking_url", bottomButtonCard.tvURL.text)
            }

        }
    }

    private fun getTrackingURL() {
        val builder = ActionParamsBuilder()
        HyperTrack.createAndAssignAction(builder.build(), object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                if (response.responseObject != null) {
                    val action = response.responseObject as? Action
                    HyperTrack.clearServiceNotificationParams()
                    bottomButtonCard.tvURL.text = action?.trackingURL
                    bottomButtonCard.hideProgress()
                    initBottomButtonCard(true, mAction)
                }
            }

            override fun onError(errorResponse: ErrorResponse) {
                AlertDialog.Builder(this@ShareLocationActivity)
                        .setTitle(getString(R.string.dialog_title_error))
                        .setMessage(errorResponse.errorMessage)
                        .setPositiveButton(getString(R.string.dialog_button_ok)) { dialogInterface,
                                                                                   _ ->
                            bottomButtonCard.hideProgress()
                            bottomButtonCard?.setShareButtonText(getString(R.string
                                    .share_textview_text_start_sharing))
                            dialogInterface.dismiss()
                        }
            }
        })
    }

    private fun initBottomButtonCard(show: Boolean, action: String?) {
        when (action) {
            AppConstants.KEY_CONFIRM -> {
                bottomButtonCard?.hideCloseButton()
                bottomButtonCard?.hideTvTitle()
                bottomButtonCard?.setDescriptionText(getString(R.string.confirm_move_map))
                bottomButtonCard?.setShareButtonText(getString(R.string.confirm_location))
                bottomButtonCard?.showActionButton()
            }
            AppConstants.KEY_SHARING, AppConstants.KEY_CURRENT_LOCATION -> {
                bottomButtonCard?.hideCloseButton()
                bottomButtonCard?.hideTvDescription()
                bottomButtonCard?.setTitleText(getString(R.string.share_textview_text_look_good))
                bottomButtonCard?.setShareButtonText(getString(R.string.share_textview_text_start_sharing))
                bottomButtonCard?.showActionButton()

            }
            else -> {
                bottomButtonCard?.showClosebutton()
                bottomButtonCard?.actionType = BottomButtonCard.ActionType.SHARE_TRACKING_URL
                bottomButtonCard?.showTrackingURLLayout()
                bottomButtonCard?.setTitleText(getString(R.string.bottom_button_card_title_text))
                bottomButtonCard?.setDescriptionText(getString(R.string.bottom_button_card_description_text))
                bottomButtonCard?.setShareButtonText(getString(R.string.share_textview_text_share_link))
                bottomButtonCard?.showActionButton()
                bottomButtonCard?.showTitle()
            }
        }
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

    /**
     * Set destination name
     */
    fun setDestinationName(name: String) {
        mDestinationName = name
    }

    private fun onClickButtonSearchLocation() {
        rlSearchLocation.setOnClickListener {
            startActivity(Intent(this, SearchLocationActivity::class.java))
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun addDestinationMarker(latLng: LatLng?) {
        if (latLng != null) {
            mGoogleMap.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                    .title(mDestinationName)
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
                    ?.showInfoWindow()
            imgPickLocation.visibility = View.INVISIBLE
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
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
        drawable.setSize(AppConstants.KEY_DRAWABLE_SIZE, AppConstants.KEY_DRAWABLE_SIZE)
        drawable.setColor(ContextCompat.getColor(this, R.color.pulse_color))

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        val groundOverlay = mGoogleMap.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, AppConstants.KEY_GROUND_OVERLAY_POSITION)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
        val groundAnimation = RadiusAnimation(groundOverlay)
        groundAnimation.repeatCount = Animation.INFINITE
        groundAnimation.duration = AppConstants.KEY_GR_ANIMATION_DUR
        mMapFragment.view?.startAnimation(groundAnimation)
    }

    private fun getLocationName(latLng: LatLng?) {
        if (latLng != null) {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                tvLocation.text = address.getAddressLine(0)
                mDestinationName = if (!address.subThoroughfare.isNullOrEmpty()) {
                    address.subThoroughfare.plus(" ").plus(address.thoroughfare)
                } else {
                    address.thoroughfare
                }
            } else {
                tvLocation.text = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.disconnect()
        if (mAction == AppConstants.KEY_CONFIRM && !mLocationAsyncTask.isCancelled) {
            mLocationAsyncTask.cancel(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mAction == AppConstants.KEY_CONFIRM && !mLocationAsyncTask.isCancelled) {
            mLocationAsyncTask.cancel(true)
        }
    }

    private fun initGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun initLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = ShareLocationActivity.INTERVAL
        mLocationRequest.fastestInterval = ShareLocationActivity.INTERVAL
    }

    override fun onConnected(p0: Bundle?) {
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this)
        }

        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this)
        }

        if (HyperTrackUtils.isInternetConnected(this)) {
            if (HyperTrackUtils.isLocationEnabled(this)) {
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        //No op
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SearchLocationActivity::class.java))
        this.finish()
    }
}
