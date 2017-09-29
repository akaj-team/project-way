package vn.asiantech.way.api

import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
interface ServiceApi {
    @GET("geocode/json")
    fun getAddressLocation(@Query("latlng") latLng: String): Call<LocationResponse>
}