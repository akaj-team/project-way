package vn.asiantech.way.ui.register

import android.content.Context
import android.content.Intent
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.extension.observeOnUiThread

/**
 *
 * Created by tien.hoang on 11/29/17.
 */
class RegisterViewModel(val context: Context) {
    internal val progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val wayRepository = WayRepository()
    private val assetDataRepository = LocalRepository(context)

    internal fun getCountries(): Observable<List<Country>> {
        return assetDataRepository.getCountries()
                .observeOnUiThread()
    }

    internal fun createUser(userParams: UserParams): Observable<ResponseStatus> {
        return wayRepository.createUser(userParams)
                .observeOnUiThread()
    }

    internal fun updateUser(userParams: UserParams): Observable<ResponseStatus> {
        progressBarStatus.onNext(true)
        return wayRepository.updateUser(userParams)
                .observeOnUiThread()
                .doOnNext { progressBarStatus.onNext(false) }
                .doOnError { progressBarStatus.onNext(false) }
    }

    internal fun getUser(): Single<User> {
        progressBarStatus.onNext(true)
        return wayRepository.getUser()
                .observeOnUiThread()
                .doOnSuccess { progressBarStatus.onNext(false) }
                .doOnError { progressBarStatus.onNext(false) }
    }

    internal fun selectAvatar(intent: Intent): Observable<Intent> {
        progressBarStatus.onNext(true)
        return assetDataRepository.getAvatarIntent(intent)
                .observeOnUiThread()
                .doOnNext { progressBarStatus.onNext(false) }
                .doOnError { progressBarStatus.onNext(false) }
    }

    internal fun saveLoginStatus(isLogin: Boolean) {
        assetDataRepository.setLoginStatus(isLogin)
    }
}
