package vn.asiantech.way.data.source

import io.reactivex.Observable
import vn.asiantech.way.data.model.Country

/**
 * Created by tien.hoang on 11/29/17.
 */
interface AssetDataSource {
    fun getCountries(): Observable<List<Country>>
}
