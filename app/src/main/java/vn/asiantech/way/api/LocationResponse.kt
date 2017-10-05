package vn.asiantech.way.api

import vn.asiantech.way.models.LocationAddress

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
data class LocationResponse(
        val results: List<LocationAddress>,
        val status: String
)
