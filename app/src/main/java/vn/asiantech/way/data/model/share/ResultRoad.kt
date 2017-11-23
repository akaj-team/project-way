package vn.asiantech.way.data.model.share

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 23/11/2017.
 */
data class ResultRoad(val snappedPoints: MutableList<LocationRoad>)

data class LocationRoad(val location: Point, val placeId: String)

data class Point(val latitude: Double, val longitude: Double)