package vn.asiantech.way.extension

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by tien.hoang on 12/1/17.
 */
internal fun <T> Observable<T>.observeOnUiThread(): Observable<T>
        = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

internal fun <T> Single<T>.observeOnUiThread(): Single<T>
        = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

