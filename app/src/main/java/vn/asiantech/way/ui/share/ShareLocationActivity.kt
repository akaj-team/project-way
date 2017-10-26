package vn.asiantech.way.ui.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_share_location.*
import kotlinx.android.synthetic.main.bottom_button_card_view.*
import kotlinx.android.synthetic.main.bottom_button_card_view.view.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.search.MyLocation
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.custom.BottomButtonCard
import vn.asiantech.way.ui.custom.RadiusAnimation
import vn.asiantech.way.ui.search.SearchLocationActivity
import vn.asiantech.way.utils.AppConstants
import vn.asiantech.way.utils.LocationUtil
import java.util.*

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class ShareLocationActivity : BaseActivity(), OnMapReadyCallback,
        LocationSource.OnLocationChangedListener,
        GoogleMap.OnCameraIdleListener {

    private var mGoogleMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mLatLng: LatLng? = null
    private var mDestinationName: String? = null
    private var mMyLocation: MyLocation? = null
    private var mAction: String? = null
    private var mIsConfirm: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        if (intent.extras != null) {
            mMyLocation = intent.getParcelableExtra(AppConstants.keyLocation)
            mAction = intent.getStringExtra(AppConstants.keyConfirm)
        }
        initMap()
        initializeUIViews()
        onClickButtonSearchLocation()
        initBottomButtonCard(true, mAction)
    }


    private fun initMap() {
        mMapFragment = supportFragmentManager.findFragmentById(R.id.fgMap) as? SupportMapFragment
        mMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        // Add a marker in Sydney and move the camera
        val currentLocation = LocationUtil(this).getCurrentLocation()
        if (currentLocation != null) {
            drawCurrentMaker(currentLocation)
        }
        mGoogleMap?.setOnCameraIdleListener(this)
        val lat = mMyLocation?.geometry?.location?.lat
        val lng = mMyLocation?.geometry?.location?.lng
        if (lat != null && lng != null) {
            mLatLng = LatLng(lat, lng)
            addDestinationMarker(mLatLng)
        }
        if (mAction == AppConstants.keyCurrentLocation) {
            mLatLng = currentLocation?.latitude?.let { LatLng(it, currentLocation.longitude) }
            addDestinationMarker(mLatLng)
            getLocationName(mLatLng)
            mIsConfirm = true
        }
    }

    override fun onLocationChanged(location: Location) {
        drawCurrentMaker(location)
    }

    override fun onCameraIdle() {
        mLatLng = mGoogleMap?.cameraPosition?.target
        if (!mIsConfirm) {
            if (mMyLocation?.geometry?.location == null) {
                getLocationName(mLatLng)
            } else {
                val lat = mMyLocation?.geometry?.location?.lat
                val lng = mMyLocation?.geometry?.location?.lng
                if (lat != null && lng != null) {
                    mLatLng = LatLng(lat, lng)
                    getLocationName(mLatLng)
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
                    AppConstants.keyConfirm -> {
                        mAction = AppConstants.keySharing
                        initBottomButtonCard(true, mAction)
                        addDestinationMarker(mLatLng)
                        mIsConfirm = true
                    }
                    AppConstants.keySharing, AppConstants.keyCurrentLocation -> {
                        mAction = AppConstants.keyStartSharing
                        initBottomButtonCard(true, mAction)
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

    private fun initBottomButtonCard(show: Boolean, action: String?) {
        when (action) {
            AppConstants.keyConfirm -> {
                bottomButtonCard?.hideCloseButton()
                bottomButtonCard?.hideTvTitle()
                bottomButtonCard?.setDescriptionText(getString(R.string.confirm_move_map))
                bottomButtonCard?.setShareButtonText(getString(R.string.confirm_location))
                bottomButtonCard?.showActionButton()
            }
            AppConstants.keySharing, AppConstants.keyCurrentLocation -> {
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
                bottomButtonCard?.setShareButtonText(getString(R.string.share_textview_text_start_sharing))
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
            mGoogleMap?.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_expected_place_marker))
                    .title(mDestinationName)
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
                    ?.showInfoWindow()
            imgPickLocation.visibility = View.INVISIBLE
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }

    private fun drawCurrentMaker(location: Location) {
        if (mGoogleMap != null) {
            mGoogleMap?.clear()
            val currentLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap?.addMarker(MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_point))
                    .title(getString(R.string.current_location))
                    .anchor(AppConstants.KEY_DEFAULT_ANCHOR, AppConstants.KEY_DEFAULT_ANCHOR))
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
            addPulseRing(currentLocation)
        }
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
        val groundOverlay = mGoogleMap?.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, AppConstants.KEY_GROUND_OVERLAY_POSITION)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap)))
        val groundAnimation = RadiusAnimation(groundOverlay)
        groundAnimation.repeatCount = Animation.INFINITE
        groundAnimation.duration = AppConstants.KEY_GR_ANIMATION_DUR
        mMapFragment?.view?.startAnimation(groundAnimation)
    }

    private fun getLocationName(latLng: LatLng?) {
        if (latLng != null) {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SearchLocationActivity::class.java))
        this.finish()
    }
}
