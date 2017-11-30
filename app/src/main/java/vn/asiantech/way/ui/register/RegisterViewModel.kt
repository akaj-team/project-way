package vn.asiantech.way.ui.register

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.AssetDataRepository

/**
 * Created by tien.hoang on 11/29/17.
 */
class RegisterViewModel(val context: Context) {
    private val assetDataRepository = AssetDataRepository(context)

    internal fun getCountries(): Observable<List<Country>> {
        return assetDataRepository.getCountries()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

}