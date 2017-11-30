package vn.asiantech.way.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */
@Parcelize
class WayLocation(@SerializedName("formatted_address") val formatAddress: String?, val id: String?,
               val name: String?, @SerializedName("place_id") val placeId: String?,
               var geometry: Geometry?, var isHistory: Boolean?) : Parcelable {
    constructor(id: String, placeId: String, name: String, formattedAddress: String)
            : this(formattedAddress, id, name, placeId, null, false)
}

/**
 *  Result of Google map api place detail
 */
data class ResultPlaceDetail(val result: WayLocation)

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
