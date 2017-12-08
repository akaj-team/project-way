package vn.asiantech.way.data.source.remote.googleapi

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.remote.response.Response

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
interface ApiService {
    @GET("geocode/json")
    fun getAddressLocation(@Query("latlng") latLng: String): Single<Response<MutableList<LocationAddress>>>

    /**
     *  This method to get detail of location.
     *
     *  @param placeId the id of location.
     */
    @GET("place/details/json")
    fun getLocationDetail(@Query("placeid") placeId: String?, @Query("key") key: String): Single<ResultPlaceDetail>

    /**
     *  This method to search location by name.
     *
     *  @param input the query to search location.
     *  @param key the api key of google place api.
     */
    @GET("place/autocomplete/json")
    fun searchLocations(@Query("input") input: String, @Query("key") key: String,
                        @Query("language") language: String = "vi", @Query("sensor") sensor: Boolean = false)
            : Single<AutoCompleteResult>

    /**
     *  This method to get time and distance location
     *
     *  @param units the units of return value type
     *  @param origins the start LatLng
     *  @param destinations the destinations LatLng
     */
    @GET("distancematrix/json")
    fun getLocationDistance(@Query("origins") origins: String, @Query("destinations") destinations: String,
                            @Query("units") units: String = "metric"): Single<ResultDistance>

    @GET
    fun getListLocation(@Url url: String): Observable<ResultRoad>
}
