package vn.asiantech.way.models

import com.google.android.gms.maps.model.LatLng

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 29/09/2017.
 */
data class Arrived(var latLngs: MutableList<LatLng>? = null, var time: Long = 0,
                   var distance: Double = 0.0, var averageSpeed: Double = 0.0)
