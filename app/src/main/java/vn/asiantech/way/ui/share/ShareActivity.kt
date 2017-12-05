package vn.asiantech.way.ui.share

import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.LocationRoad
import vn.asiantech.way.data.model.Rows
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.utils.AppConstants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 27/11/2017.
 */
class ShareActivity : BaseActivity(), GoogleMap.OnCameraIdleListener, LocationListener {

    private lateinit var shareViewModel: ShareViewModel
    private lateinit var shareActivityUI: ShareActivityUI
    private lateinit var supportMapFragment: SupportMapFragment
    private val ShareObserverble = PublishSubject.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareActivityUI = ShareActivityUI()
        shareActivityUI.setContentView(this)
        shareViewModel = ShareViewModel(this)

        when (intent.action) {
            AppConstants.ACTION_CURRENT_LOCATION -> {
                // TODO: 01/12/2017
                // Get current location and do more...
                Log.i("tag11", "1111111111111")
            }

            AppConstants.ACTION_CHOOSE_ON_MAP -> {
                // TODO: 01/12/2017
                // Choose location on the map and do more...
                Log.i("tag11", "2222222222")
            }

            AppConstants.ACTION_SEND_WAY_LOCATION -> {
                val location = intent.extras.getParcelable<WayLocation>(AppConstants.KEY_LOCATION)
                // TODO: 01/12/2017
                // Do more...
                Log.i("tag11", location.formatAddress)
            }

            else -> {
                // TODO: 01/12/2017
                // This is tracking case
            }
        }
        supportMapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.share_activity_map, supportMapFragment).commit()
        supportMapFragment.getMapAsync { googleMap ->
            googleMap.setOnCameraIdleListener(this)
        }
    }

    override fun onBindViewModel() {
        addDisposables(shareViewModel.getLocationDistance("metric",
                "16.0795181,108.2363563", "16.0803531,108.2345323")
                .observeOnUiThread()
                .subscribe(this::loadDistanceETA),
                shareViewModel.getListLocation("https://roads.googleapis.com/v1/snapToRoads?path=16.0802401," +
                        "108.2365923|16.080348,%20108.236753&interpolate=true&key=AIzaSyCZc4PAEpeVC18QnS5fPBt5hk3EFMbFjj8")
                        .observeOnUiThread()
                        .subscribe(this::loadListLocationWhenReOpen),
                shareViewModel.bbb
                        .observeOnUiThread()
                        .subscribe(this::getBatteryCapacity))
    }

    override fun onLocationChanged(location: Location) {
    }

    override fun onCameraIdle() {

    }

    private fun setCurrentLocation() {

    }

    // Get ETA Distance and duration
    private fun loadDistanceETA(row: List<Rows>) {
    }

    // Get list lat lng direction
    private fun loadListLocationWhenReOpen(list: List<LocationRoad>) {
    }

    private fun getBatteryCapacity(batteryCapacity: Int) {
        //todo set text to tvBattery
        Log.d("zxc", "aa  " + batteryCapacity)
    }

    private fun getCalendarDateTime(): String {
        val calendar = Calendar.getInstance().time
        val simple = SimpleDateFormat("KK:mm a', Th'MM dd", Locale.ENGLISH)
        return simple.format(calendar)
    }
}
