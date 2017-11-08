package vn.asiantech.way.data.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */
@Parcelize
class MyLocation(var isHistory: Boolean?, @SerializedName("formatted_address") val formattedAddress: String?, val id: String?, val name: String?,
                 @SerializedName("place_id") val placeId: String?, var geometry: Geometry?) : Parcelable {
    constructor(id: String, placeId: String, name: String, formattedAddress: String)
            : this(false, formattedAddress, id, name, placeId, null)
}

/**
 *  Result of Google map api place detail
 */
data class ResultPlaceDetail(val result: MyLocation)

/**
 *  Geometry of location
 */
@Parcelize
data class Geometry(var location: Coordinates) : Parcelable

/**
 *  Coordinates of location
 */
@Parcelize
data class Coordinates(var lat: Double, var lng: Double) : Parcelable
