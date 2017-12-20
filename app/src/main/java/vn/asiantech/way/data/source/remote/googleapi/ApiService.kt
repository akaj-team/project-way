package vn.asiantech.way.data.source.remote.googleapi

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import vn.asiantech.way.BuildConfig
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
    @GET("place/details/json?key=" + BuildConfig.GOOGLE_MAP_API_KEY)
    fun getLocationDetail(@Query("placeid") placeId: String?): Single<ResultPlaceDetail>

    /**
     *  This method to search location by name.
     *
     *  @param input the query to search location.
     *  @param key the api key of google place api.
     */
    @GET("place/autocomplete/json?key=" + BuildConfig.GOOGLE_MAP_API_KEY)
    fun searchLocations(@Query("input") input: String, @Query("language") language: String = "vi",
                        @Query("sensor") sensor: Boolean = false)
            : Single<AutoCompleteResult>

    /**
     *  This method to get time and distance location
     *
     *  @param units the units of return value type
     *  @param origins the start LatLng
     *  @param destinations the destinations LatLng
     */
    @GET("distancematrix/json")
    fun getLocationDistance(@Query("origins") origin: String, @Query("destinations") destination: String,
                            @Query("units") units: String = "metric"): Single<ResultDistance>

    /**
     *  This method to get list LatLng
     *
     *  @param url the link api
     */
    @GET
    fun getListLocation(@Url url: String): Observable<ResultRoad>
}
