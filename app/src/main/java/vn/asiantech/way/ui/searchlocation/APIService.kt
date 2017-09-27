package vn.asiantech.way.ui.searchlocation

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */

interface APIService {

    @GET("json")
    fun getLocation(@Query("query") query: String, @Query("key") key: String): Call<APIResult>
}
