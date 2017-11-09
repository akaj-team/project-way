package vn.asiantech.way.data.model.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */
@Parcelize
class MyLocation(val isHistory: Boolean?, val formattedAddress: String?, val id: String?, val name: String?,
                      val placeId : String?, val geometry: Geometry?) : Parcelable

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
