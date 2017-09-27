package vn.asiantech.way.ui.share

import android.content.Context
import com.google.android.gms.maps.CameraUpdate
import com.hypertrack.lib.HyperTrackMapAdapter
import com.hypertrack.lib.HyperTrackMapFragment
import vn.asiantech.way.R

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 27/09/2017.
 */
class MapAdapter(context: Context) : HyperTrackMapAdapter(context) {
    override fun getMapFragmentInitialState(hyperTrackMapFragment: HyperTrackMapFragment?): CameraUpdate {
        return super.getMapFragmentInitialState(hyperTrackMapFragment)
    }

    override fun showPlaceSelectorView(): Boolean = true

    override fun showTrailingPolyline(): Boolean = true

    override fun showTrafficLayer(hyperTrackMapFragment: HyperTrackMapFragment?): Boolean = false

    override fun enableLiveLocationSharingView(): Boolean = true

    override fun getResetBoundsButtonIcon(hyperTrackMapFragment: HyperTrackMapFragment?): Int = R.drawable.ic_reset_bounds_button

    override fun showLocationDoneButton(): Boolean = false

    override fun showSourceMarkerForActionID(hyperTrackMapFragment: HyperTrackMapFragment?, actionID: String): Boolean = true

    override fun showLiveLocationSharingSummaryView(): Boolean = true
}