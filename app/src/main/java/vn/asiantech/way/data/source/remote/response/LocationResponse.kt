package vn.asiantech.way.data.source.remote.response

import vn.asiantech.way.data.model.LocationAddress

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
data class LocationResponse(val results: List<LocationAddress>, val status: String)
