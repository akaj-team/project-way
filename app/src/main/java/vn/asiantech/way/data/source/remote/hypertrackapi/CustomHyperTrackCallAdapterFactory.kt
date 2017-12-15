package vn.asiantech.way.data.source.remote.hypertrackapi

/**
 * Created by tien.hoang on 12/6/17.
 */

import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import vn.asiantech.way.data.source.remote.util.BaseRxCallAdapterWrapper
import java.lang.reflect.Type

/**
 * Custom error handler for Hyper TRack
 */
class CustomHyperTrackCallAdapterFactory : CallAdapter.Factory() {
    private var original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    companion object {
        /**
         * Create instance
         */
        fun create(): CallAdapter.Factory = CustomHyperTrackCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit)
            : CallAdapter<*, *>?
            = BaseRxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit))

//    private inner class RxCallAdapterWrapper<R>(private val retrofit: Retrofit, private val wrapped: CallAdapter<R, *>?) : BaseRxCallAdapterWrapper<R>(retrofit, wrapped) {
//        override fun convertRetrofitExceptionToCustomException(throwable: Throwable): Throwable {
//            if (throwable is HttpException) {
//            }
//            return throwable
//        }
//
//        override fun createExceptionForSuccessResponse(response: Any?): Throwable? {
//            if (response is Group) {
//                if (response.name == "testingGroup") {
//                    return ApiException("ttteeesss")
//                }
//            }
//            return super.createExceptionForSuccessResponse(response)
//        }
//    }
}
