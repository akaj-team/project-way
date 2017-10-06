package vn.asiantech.way.models

import com.google.gson.annotations.SerializedName

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
data class LocationAddress(
        @SerializedName("formatted_address")
        val address:String,
        @SerializedName("place_id")
        val placeId:String
)
