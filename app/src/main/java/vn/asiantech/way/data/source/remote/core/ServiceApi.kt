package vn.asiantech.way.data.source.remote.core

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.asiantech.way.data.model.AutoCompleteResult
import vn.asiantech.way.data.model.ResultPlaceDetail
import vn.asiantech.way.data.source.remote.response.LocationResponse

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
interface ServiceApi {
    @GET("geocode/json")
    fun getAddressLocation(@Query("latlng") latLng: String): Call<LocationResponse>

    /**
     *  This method to get detail of location.
     *
     *  @param placeId the id of location.
     */
    @GET("place/details/json")
    fun getLocationDetail(@Query("placeid") placeId: String?, @Query("key") key: String): Call<ResultPlaceDetail>

    /**
     *  This method to search location by name.
     *
     *  @param input the query to search location.
     *  @param key the api key of google place api.
     */
    @GET("place/autocomplete/json")
    fun searchLocations(@Query("input") input: String, @Query("key") key: String,
                        @Query("language") language: String = "vi", @Query("sensor") sensor: Boolean = false)
            : Call<AutoCompleteResult>
}