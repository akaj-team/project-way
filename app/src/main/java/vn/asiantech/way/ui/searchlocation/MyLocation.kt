package vn.asiantech.way.ui.searchlocation

/**
 * MyLocation.
 *
 * @author CuongCV
 */

class MyLocation {
    var formatted_address: String? = null
    var id: String? = null
    var name: String? = null
    var place_id: String? = null
    var geometry: Geometry? = null

    constructor() {}

    constructor(formatted_address: String?, id: String?, name: String?, place_id: String?, geometry: Geometry?) {
        this.formatted_address = formatted_address
        this.id = id
        this.name = name
        this.place_id = place_id
        this.geometry = geometry
    }


}

data class Geometry(var location: Coordinates)

data class Coordinates(var lat: Double, var lng: Double)
