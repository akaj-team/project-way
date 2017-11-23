package vn.asiantech.way.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.asiantech.way.data.model.share.ResultRoad

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 23/11/2017.
 */
interface RoadServiceApi {
    @GET("snapToRoads")
    fun getListLocation(@Query("path") path: String,
                        @Query("key") key: String,
                        @Query("interpolate") interpolate: Boolean = true): Call<ResultRoad>
}