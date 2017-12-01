package vn.asiantech.way.data.source.datasource

import io.reactivex.Observable
import vn.asiantech.way.data.model.Country

/**
 * Created by tien.hoang on 12/1/17.
 */
interface LocalDataSource {
    fun getCountries(): Observable<List<Country>>

    fun getUserToken(): String
    fun setUserToken(token: String)
}