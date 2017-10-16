package vn.asiantech.way.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.asiantech.way.data.model.LocationResponse

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
interface ServiceApi {
    @GET("geocode/json")
    fun getAddressLocation(@Query("latlng") latLng: String): Call<LocationResponse>
}
