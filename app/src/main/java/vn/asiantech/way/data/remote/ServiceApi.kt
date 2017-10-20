package vn.asiantech.way.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.asiantech.way.data.model.search.ResultLocation
import vn.asiantech.way.data.model.share.LocationResponse

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
interface ServiceApi {
    @GET("geocode/json")
    fun getAddressLocation(@Query("latlng") latLng: String): Call<LocationResponse>

    /**
     *  This method to search location by name
     *
     *  @param query the query to search location
     *  @param key the api key of google place api
     */
    @GET("place/textsearch/json")
    fun getLocation(@Query("query") query: String, @Query("key") key: String): Call<ResultLocation>
}
