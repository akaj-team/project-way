package vn.asiantech.way.ui.searchlocation

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * APIService.
 *
 * @author CuongCV
 */

interface APIService {

    @GET("json")
    fun getLocation(@Query("query") query: String, @Query("key") key: String): Call<APIResult>
}
