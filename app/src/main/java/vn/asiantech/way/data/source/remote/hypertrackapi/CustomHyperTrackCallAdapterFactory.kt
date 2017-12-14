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
}
