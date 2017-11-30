package vn.asiantech.way.ui.register

import android.content.Context
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.AssetDataRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.data.source.remote.response.ResponseStatus

/**
 * Created by tien.hoang on 11/29/17.
 */
class RegisterViewModel(val context: Context) {
    private val assetDataRepository = AssetDataRepository(context)
    private val wayRepository = WayRepository()

    internal fun getCountries(): Observable<List<Country>> {
        return assetDataRepository.getCountries()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    internal fun createUser(userParams: UserParams): Observable<ResponseStatus> {
        return wayRepository.createUser(userParams)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    internal fun updateUser(userParams: UserParams): Observable<ResponseStatus> {
        return wayRepository.updateUser(userParams)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }
}
