package vn.asiantech.way.interfaces

import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import vn.asiantech.way.models.Direction

/**
 *  Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 26/09/2017.
 */
interface OnGetApiArrived {
    companion object {
        const val URL_DIRECTION="/maps/api/directions/json"
    }

    @GET(URL_DIRECTION)
    fun getDirection(@Query("origin") origin: LatLng, @Query("destination") destination:LatLng, @Query("sensors") sensors:Boolean): Call<Direction>
}