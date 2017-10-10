package vn.asiantech.way.ui.in_progress

import android.Manifest
import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.HyperTrackUtils
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.internal.consumer.view.MarkerAnimation
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.HyperTrackLocation
import com.hypertrack.lib.models.SuccessResponse
import kotlinx.android.synthetic.main.fragment_progress_location.*
import vn.asiantech.way.R


/**
 * A simple [Fragment] subclass.
 */
class ProgressLocationFragment : Fragment(), OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null
    private var mCurrentLocationMarker: Marker? = null
    private var mCircle: GroundOverlay? = null
    private var mValueAnimator: ValueAnimator? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_progress_location, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkGPS()
        initView()
        addEvents()
    }

    private fun initView() {
        val mapFragment = activity.fragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
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
        rippleViewStop.setOnClickListener { Toast.makeText(context, "STOP STOP STOP", Toast.LENGTH_SHORT).show() }
        rippleViewShare.setOnClickListener { Toast.makeText(context, "SHARE SHARE SHARE", Toast.LENGTH_SHORT).show() }
    }

    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap = p0
        /*ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1)
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1)*/
        mGoogleMap?.addMarker(MarkerOptions()
                .title("Đà Nẵng đó!")
                .position(LatLng(16.074812, 108.233132))
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_hero_marker))
        )
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(16.074812, 108.233132), 12f))
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

    fun updateCurrentLocation(location: HyperTrackLocation?) {
        if (location?.geoJSONLocation == null || location.geoJSONLocation.latLng == null) {
            HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
                override fun onSuccess(p0: SuccessResponse) {
                    Log.d("at-dinhvo", "onSuccess: Current Location Recieved");
                    val hyperTrackLocation = HyperTrackLocation(p0.responseObject as? Location)
                    updateCurrentLocation(hyperTrackLocation)
                }

                override fun onError(p0: ErrorResponse) {
                    Log.d("at-dinhvo", "onError: Current Location Receiving error");
                    Log.d("at-dinhvo", "onError: " + p0.errorMessage)
                }
            })
            return
        }
        val latLng = location.geoJSONLocation.latLng
        if (mCurrentLocationMarker == null) {
            mCurrentLocationMarker = mGoogleMap?.addMarker(MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_source_place_marker))
                    .anchor(0.5f, 0.5f));
            addPulseRing(latLng)
        } else {
            currentLocationMarker.setVisible(true);
            circle.setPosition(latLng);
            circle.setVisible(true);
            MarkerAnimation.animateMarker(currentLocationMarker, latLng);
        }
        startPulse(false);
        if (!isRestoreLocationSharing && !isHandleTrackingUrlDeeplink)
            updateMapView();
    }

    private fun startPulse(reset: Boolean) {

        if (!HyperTrackUtils.isInternetConnected(this)) {
            if (circle != null) {
                circle.remove()
            }
            if (valueAnimator != null) {
                valueAnimator.cancel()
            }
        }
        if (valueAnimator == null || reset) {
            if (valueAnimator != null)
                valueAnimator.end()
            val radius = intArrayOf(circleRadius)
            valueAnimator = ValueAnimator()
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE)
            valueAnimator.setRepeatMode(ValueAnimator.RESTART)
            valueAnimator.setIntValues(0, radius[0].toInt())
            valueAnimator.setDuration(2000)
            valueAnimator.setEvaluator(IntEvaluator())
            valueAnimator.setInterpolator(AccelerateDecelerateInterpolator())
            valueAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
                val animatedFraction = valueAnimator.animatedFraction
                circle.setDimensions(animatedFraction * radius[0].toInt())
                circle.setTransparency(animatedFraction)
            })
            valueAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    circle.setTransparency(1f)
                    circle.setVisible(true)
                    if (currentLocationMarker != null)
                        currentLocationMarker.setVisible(true)
                }

                override fun onAnimationEnd(animation: Animator) {

                }

                override fun onAnimationCancel(animation: Animator) {
                    ObjectAnimator.ofFloat(circle, "transparency", 1f, 0f).setDuration(500).start()
                    circle.setVisible(false)
                    if (currentLocationMarker != null)
                        currentLocationMarker.setVisible(false)
                }

                override fun onAnimationRepeat(animation: Animator) {
                    //radius[0] = circleRadius;
                }
            })
        }

        valueAnimator.start()
    }

    private fun stopPulse() {
        if (valueAnimator != null) {
            valueAnimator.cancel()
        }
    }

    private fun addPulseRing(latLng: LatLng) {
        val d = GradientDrawable()
        d.shape = GradientDrawable.OVAL
        d.setSize(500, 500)
        d.setColor(ContextCompat.getColor(this, R.color.pulse_color))

        val bitmap = Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)

        // Convert the drawable to bitmap
        val canvas = Canvas(bitmap)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)

        // Radius of the circle
        val radius = 100

        // Add the circle to the map
        circle = mMap.addGroundOverlay(GroundOverlayOptions()
                .position(latLng, (2 * radius).toFloat()).image(BitmapDescriptorFactory.fromBitmap(bitmap)))
    }

    private fun startPulse(reset: Boolean) {
        if (!HyperTrackUtils.isInternetConnected(this)) {
            if (mCircle != null) {
                mCircle.remove()
            }
            if (valueAnimator != null) {
                valueAnimator.cancel()
            }
        }
        if (valueAnimator == null || reset) {
            if (valueAnimator != null)
                valueAnimator.end()
            val radius = intArrayOf(circleRadius)
            valueAnimator = ValueAnimator()
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE)
            valueAnimator.setRepeatMode(ValueAnimator.RESTART)
            valueAnimator.setIntValues(0, radius[0].toInt())
            valueAnimator.setDuration(2000)
            valueAnimator.setEvaluator(IntEvaluator())
            valueAnimator.setInterpolator(AccelerateDecelerateInterpolator())
            valueAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
                val animatedFraction = valueAnimator.animatedFraction
                circle.setDimensions(animatedFraction * radius[0].toInt())
                circle.setTransparency(animatedFraction)
            })
            valueAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    circle.setTransparency(1f)
                    circle.setVisible(true)
                    if (currentLocationMarker != null)
                        currentLocationMarker.setVisible(true)
                }

                override fun onAnimationEnd(animation: Animator) {

                }

                override fun onAnimationCancel(animation: Animator) {
                    ObjectAnimator.ofFloat(circle, "transparency", 1f, 0f).setDuration(500).start()
                    circle.setVisible(false)
                    if (currentLocationMarker != null)
                        currentLocationMarker.setVisible(false)
                }

                override fun onAnimationRepeat(animation: Animator) {
                    //radius[0] = circleRadius;
                }
            })
        }
    }

/*private void updateCurrentLocationMarker(final HyperTrackLocation location) {
        if (!showCurrentLocationMarker) {
            if (currentLocationMarker != null)
                currentLocationMarker.remove();
            currentLocationMarker = null;
            return;
        }
        if (ActionManager.getSharedManager(this).isActionLive()) {
            if (circle != null) {
                stopPulse();
            }
            return;
        }

        if (location == null || location.getGeoJSONLocation() == null ||
                location.getGeoJSONLocation().getLatLng() == null) {
            HyperTrack.getCurrentLocation(new HyperTrackCallback() {
                @Override
                public void onSuccess(@NonNull SuccessResponse response) {
                    Log.d(TAG, "onSuccess: Current Location Recieved");
                    HyperTrackLocation hyperTrackLocation =
                            new HyperTrackLocation((Location) response.getResponseObject());
                    SharedPreferenceManager.setLastKnownLocation((Location) response.getResponseObject());
                    updateCurrentLocationMarker(hyperTrackLocation);
                }

                @Override
                public void onError(@NonNull ErrorResponse errorResponse) {
                    Log.d(TAG, "onError: Current Location Receiving error");
                    Log.d(TAG, "onError: " + errorResponse.getErrorMessage());
                }
            });
            return;
        }
        LatLng latLng = location.getGeoJSONLocation().getLatLng();
        if (currentLocationMarker == null) {
            currentLocationMarker = mMap.addMarker(new MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ht_source_place_marker))
                    .anchor(0.5f, 0.5f));
            addPulseRing(latLng);
        } else {
            currentLocationMarker.setVisible(true);
            circle.setPosition(latLng);
            circle.setVisible(true);
            MarkerAnimation.animateMarker(currentLocationMarker, latLng);
        }
        startPulse(false);
        if (!isRestoreLocationSharing && !isHandleTrackingUrlDeeplink)
            updateMapView();
    }*/

/*void getCurrentLocation(@NonNull final HyperTrackCallback callback) {
        this.getCurrentLocation((Integer)null, new HyperTrackCallback() {
            public void onSuccess(@NonNull SuccessResponse response) {
                HyperTrackLocation hyperTrackLocation = (HyperTrackLocation)response.getResponseObject();
                Location location = new Location(hyperTrackLocation.getProvider());
                if(hyperTrackLocation.getGeoJSONLocation() != null) {
                    location.setLatitude(hyperTrackLocation.getGeoJSONLocation().getLatitude());
                    location.setLongitude(hyperTrackLocation.getGeoJSONLocation().getLongitude());
                }

                if(hyperTrackLocation.getAccuracy() != null) {
                    location.setAccuracy(hyperTrackLocation.getAccuracy().floatValue());
                }

                if(hyperTrackLocation.getSpeed() != null) {
                    location.setSpeed(hyperTrackLocation.getSpeed().floatValue());
                }

                if(hyperTrackLocation.getBearing() != null) {
                    location.setBearing(hyperTrackLocation.getBearing().floatValue());
                }

                if(hyperTrackLocation.getAltitude() != null) {
                    location.setAltitude(hyperTrackLocation.getAltitude().doubleValue());
                }

                if(callback != null) {
                    callback.onSuccess(new SuccessResponse(location));
                }

            }

            public void onError(@NonNull ErrorResponse errorResponse) {
                if(callback != null) {
                    callback.onError(errorResponse);
                }

            }
        });*/