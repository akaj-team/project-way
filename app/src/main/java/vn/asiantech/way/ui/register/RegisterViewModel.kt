package vn.asiantech.way.ui.register

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.support.v7.util.DiffUtil
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.extension.getCountryByIso
import vn.asiantech.way.extension.toBase64
import vn.asiantech.way.ui.base.Diff
import vn.asiantech.way.utils.AppConstants

/**
 *
 * Created by tien.hoang on 11/29/17.
 */
class RegisterViewModel(private val wayRepository: WayRepository, private val assetDataRepository: LocalRepository, var isRegister: Boolean) {
    internal val createDefaultUserStatus = PublishSubject.create<UserParams>()
    internal val progressBarStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    internal val backStatus: PublishSubject<Boolean> = PublishSubject.create()
    internal val countryObservable: SingleSubject<Country> = SingleSubject.create()
    internal val countries = mutableListOf<Country>()
    private var lastClickTime = 0L
    private lateinit var user: User

    constructor(context: Context, isRegister: Boolean) : this(WayRepository(), LocalRepository(context), isRegister)

    internal fun getCountries(): Observable<DiffUtil.DiffResult> {
        return assetDataRepository.getCountries()
                .map {
                    val diff = Diff(countries, it)
                            .areItemsTheSame { oldItem, newItem ->
                                oldItem.iso == newItem.iso
                            }
                            .areContentsTheSame { oldItem, newItem ->
                                oldItem.tel == newItem.tel &&
                                        oldItem.countryName == newItem.countryName
                            }
                            .calculateDiff()
                    countries.clear()
                    countries.addAll(it)
                    diff
                }
    }

    internal fun createUser(userParams: UserParams): Observable<ResponseStatus> = wayRepository.createUser(userParams)

    internal fun updateUser(userParams: UserParams): Observable<ResponseStatus> = wayRepository.updateUser(userParams)
            .doOnSubscribe { progressBarStatus.onNext(true) }
            .doOnNext { progressBarStatus.onNext(false) }
            .doOnError { progressBarStatus.onNext(false) }

    internal fun isEnableUpdateButton(name: String, phone: String, avatar: Bitmap?): Boolean {
        val numberCharRemove = phone.split("/")[0].length + 1
        if (name == user.name && phone == user.phone.removeRange(0, numberCharRemove)
                && avatar != null) {
            return false
        }
        return true
    }

    internal fun getUser(): Single<User> {
        progressBarStatus.onNext(true)
        return wayRepository.getUser()
                .doOnSuccess {
                    user = it
                    val basePhone: List<String>? = user.phone?.split("/")
                    if (basePhone != null) {
                        val isoCode = basePhone[0]
                        countries.getCountryByIso(isoCode)?.let {
                            countryObservable.onSuccess(it)
                        }
                    }
                    progressBarStatus.onNext(false)
                }
                .doOnError { progressBarStatus.onNext(false) }
    }

    internal fun saveLoginStatus(isLogin: Boolean) {
        assetDataRepository.setLoginStatus(isLogin)
    }

    internal fun generateUserInformation(name: String, phone: String,
                                         isoCode: String?, avatar: Bitmap?) = UserParams()
            .setName(name)
            .setPhoto(avatar?.toBase64())
            .setPhone(isoCode.plus("/").plus(phone))
            .setLookupId(phone)

    internal fun createUserDefault(userName: String) {
        val userParam = UserParams()
        userParam.name = userName
        createDefaultUserStatus.onNext(userParam)
    }

    internal fun eventBackPressed() {
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
