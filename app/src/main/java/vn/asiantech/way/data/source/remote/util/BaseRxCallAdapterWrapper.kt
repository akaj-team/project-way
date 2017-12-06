package vn.asiantech.way.data.source.remote.util

import io.reactivex.*
import io.reactivex.subjects.MaybeSubject
import io.reactivex.subjects.SingleSubject
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Override this class to handle http exception and convert api success to error
 */
open class BaseRxCallAdapterWrapper<R>(private val retrofit: Retrofit, private val wrapped: CallAdapter<R, *>?) : CallAdapter<R, Any> {
    override fun adapt(call: Call<R>): Any? {
        val adapt = wrapped?.adapt(call)
        return when (adapt) {
            is Maybe<*> -> adapt.map(this::handleCustomError).onErrorResumeNext(this::handleMaybeRetrofitError)
            is Single<*> -> adapt.map(this::handleCustomError).onErrorResumeNext(this::handleSingleRetrofitError)
            is Flowable<*> -> adapt.map(this::handleCustomError).onErrorResumeNext(this::handleFlowableRetrofitError)
            is Observable<*> -> adapt.map(this::handleCustomError).onErrorResumeNext(this::handleObservableRetrofitError)
            else -> null
        }

    }

    private fun handleMaybeRetrofitError(t: Throwable): MaybeSource<R>? {
        return MaybeSubject.error<R> { convertRetrofitExceptionToCustomException(t) }
    }

    private fun handleSingleRetrofitError(t: Throwable): SingleSource<R>? {
        return SingleSubject.error<R> { convertRetrofitExceptionToCustomException(t) }
    }

    private fun handleFlowableRetrofitError(t: Throwable): Flowable<R>? {
        return Flowable.error<R> { convertRetrofitExceptionToCustomException(t) }
    }

    private fun handleObservableRetrofitError(t: Throwable): ObservableSource<R>? {
        return Observable.error { convertRetrofitExceptionToCustomException(t) }
    }

    /**
     * Generate app api exception
     */
    open fun convertRetrofitExceptionToCustomException(throwable: Throwable): Throwable {
        return throwable
    }

    /**
     * Some resful api handle error in success response, this method is used to return exception
     */
    open fun createExceptionForSuccessResponse(response: Any?): Throwable? {
        return null
    }

    private fun handleCustomError(response: Any): Any {
        if (createExceptionForSuccessResponse(response) != null) {
            throw createExceptionForSuccessResponse(response)!!
        }
        return response
    }

    override fun responseType(): Type {
        return wrapped!!.responseType()
    }
}
