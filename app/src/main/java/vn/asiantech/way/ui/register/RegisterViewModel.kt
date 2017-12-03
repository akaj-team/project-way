package vn.asiantech.way.ui.register

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    internal fun updateUser(userParams: UserParams): Observable<ResponseStatus> {
        return wayRepository.updateUser(userParams)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    internal fun getUser(): Observable<User> {
        progressBarStatus.onNext(true)
        return wayRepository.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext { progressBarStatus.onNext(false) }
    }

    internal fun selectAvatar(intent: Intent?): Observable<Intent> {
        Log.d("xxx", "avatar: " + (intent == null))
        val result = PublishSubject.create<Intent>()
        result.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    result.onNext(intent!!)
                }
        return result
    }

    internal fun avatar(intent: Intent?): Observable<Intent> {
        val result = PublishSubject.create<Intent>()
        result.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        return result
    }
}
