package vn.asiantech.way.ui.searchlocation

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */

class MyLocation {
    var formatted_address: String? = null
    var id: String? = null
    var name: String? = null
    var place_id: String? = null
    var geometry: Geometry? = null
}

data class Geometry(var location: Coordinates)

data class Coordinates(var lat: Double, var lng: Double)
