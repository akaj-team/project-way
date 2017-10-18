package vn.asiantech.way.data.model.arrived

import com.google.android.gms.maps.model.LatLng

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 29/09/2017.
 */
data class Arrived(var latLngs: MutableList<LatLng>? = null, var dateTimeFirst: String = "",
                   var dateTimeEnd: String = "", var time: Long = 0, var distance: Double = 0.0,
                   var averageSpeed: Double = 0.0, var firstLocation: String = "",
                   var endLocation: String = "")
