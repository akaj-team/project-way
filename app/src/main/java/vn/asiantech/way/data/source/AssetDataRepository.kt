package vn.asiantech.way.data.source

import android.content.Context
import io.reactivex.Observable
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.local.AssetLocalDataSource

/**
 * Created by tien.hoang on 11/29/17.
 */
class AssetDataRepository(val context: Context) : AssetDataSource {
    override fun getCountries(): Observable<List<Country>> {
        return AssetLocalDataSource(context).getCountries()
    }
}