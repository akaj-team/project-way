package vn.asiantech.way.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.asiantech.way.data.model.search.AutoCompleteResult
import vn.asiantech.way.data.model.search.ResultPlaceDetail
import vn.asiantech.way.data.model.share.LocationResponse
import vn.asiantech.way.data.model.share.ResultDistance

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

    /**
     *  This method to get time and distance location
     *
     *  @param units the units of return value type
     *  @param origins the start LatLng
     *  @param destinations the destinations LatLng
     */
    @GET("distancematrix/json")
    fun getLocationDistance(@Query("units") units: String, @Query("origins") origins: String, @Query("destinations") destinations: String): Call<ResultDistance>
}
