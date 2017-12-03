package vn.asiantech.way.data.source.datasource

import android.content.Intent
import io.reactivex.Observable
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.model.WayLocation

/**
 *
 * Created by tien.hoang on 12/1/17.
 */
interface LocalDataSource {
    /**
     * This method get list country from local json
     *
     * @return List country object which is Observable.
     */
    fun getCountries(): Observable<List<Country>>

    /**
     * This method get login status
     *
     * @return login status which is boolean
     */
    fun getLoginStatus(): Boolean

    /**
     * This method set login status
     *
     * @param isLogin login status
     */
    fun setLoginStatus(isLogin: Boolean)

    /**
     * This method get search history of user
     *
     * @return List location
     */
    fun getSearchHistory(): List<WayLocation>?

    /**
     * This method save search history of user
     *
     * @param location
     */
    fun saveSearchHistory(location: WayLocation)

    /**
     * This method get avatar of user
     *
     * @param intent contain uri or bitmap of avatar
     * @return Intent object which is Observable.
     */
    fun getAvatarIntent(intent: Intent): Observable<Intent>
}
