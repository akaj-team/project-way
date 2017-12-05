package vn.asiantech.way.data.model

import com.google.gson.annotations.SerializedName

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 04/12/2017.
 */
data class ResultRoad(@SerializedName("snappedPoints") val snappedPoints: List<LocationRoad>)

data class LocationRoad(@SerializedName("location") val location: Point,
                        @SerializedName("placeId") val placeId: String)

data class Point(val latitude: Double, val longitude: Double)
