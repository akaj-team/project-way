package vn.asiantech.way.ui.searchlocation

import com.google.gson.annotations.SerializedName

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */

class MyLocation {
    var isHistory: Boolean? = null
    @SerializedName("formattedAddress")
    var formattedAddress: String? = null
    var id: String? = null
    var name: String? = null
    @SerializedName("placeId")
    var placeId: String? = null
    var geometry: Geometry? = null
}

data class Geometry(var location: Coordinates)

data class Coordinates(var lat: Double, var lng: Double)
