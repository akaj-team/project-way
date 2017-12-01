package vn.asiantech.way.ui.register

import android.content.Context
import android.content.Intent
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.AssetDataRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.data.source.remote.response.ResponseStatus

/**
 * Created by tien.hoang on 11/29/17.
 */
class RegisterViewModel(val context: Context) {
    private val assetDataRepository = AssetDataRepository(context)
    internal val progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val wayRepository = WayRepository()
    private var intent: Intent? = null

    internal fun getCountries(): Observable<List<Country>> {
        return assetDataRepository.getCountries()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
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

    internal fun selectAvatar(): Observable<Intent> {
        val result = AsyncSubject.create<Intent>()
        if (intent != null) {
            result.onNext(intent!!)
            result.onComplete()
            result.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
        }
        return result
    }

    internal fun setIntent(intentData: Intent){
        intent = intentData
    }
}
