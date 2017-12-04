package vn.asiantech.way.ui.register

import android.content.Context
import io.reactivex.Observable
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * Created by tien.hoang on 11/29/17.
 */
class RegisterViewModel(val context: Context) {
    private val assetDataRepository = LocalRepository(context)

    internal fun getCountries(): Observable<List<Country>> {
        return assetDataRepository.getCountries()
                .observeOnUiThread()
    }

}
