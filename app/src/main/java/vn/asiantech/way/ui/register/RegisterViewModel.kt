package vn.asiantech.way.ui.register

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.SystemClock
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.extension.toBase64
import vn.asiantech.way.utils.AppConstants

/**
 *
 * Created by tien.hoang on 11/29/17.
 */
class RegisterViewModel(val context: Context, val isRegister: Boolean) {
    internal val progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    internal val backStatus: PublishSubject<Boolean> = PublishSubject.create()
    private val wayRepository = WayRepository()
    private val assetDataRepository = LocalRepository(context)
    private var lastClickTime = 0L

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
        val result = wayRepository.getUser()
        if (!isRegister) {
            progressBarStatus.onNext(true)
            result.observeOnUiThread()
                    .doOnSuccess { progressBarStatus.onNext(false) }
                    .doOnError { progressBarStatus.onNext(false) }
        }
        return result
    }

    internal fun saveLoginStatus(isLogin: Boolean) {
        assetDataRepository.setLoginStatus(isLogin)
    }

    internal fun generateUserInformation(name: String, phone: String,
                                         isoCode: String?, avatar: Bitmap?): UserParams {
        return UserParams()
                .setName(name)
                .setPhoto(avatar?.toBase64())
                .setPhone(isoCode.plus("/").plus(phone))
                .setLookupId(phone)
    }

    internal fun getAvatar(): Observable<Intent> {
        val result = PublishSubject.create<Intent>()
        return result
    }

    internal fun onSkipClicked(userParams: UserParams): Observable<UserParams> {
        val result = PublishSubject.create<UserParams>()
        return result
    }

    internal fun onBackPress() {
        backStatus.onNext(handleBackKeyEvent())
    }

    private fun handleBackKeyEvent(): Boolean {
        if (SystemClock.elapsedRealtime() - lastClickTime < AppConstants.BACK_PRESS_DELAY) {
            return true
        }
        lastClickTime = SystemClock.elapsedRealtime()
        return false
    }
}
