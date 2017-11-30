package vn.asiantech.way.data.source.remote.googleapi

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.asiantech.way.data.source.remote.core.BaseServiceFactory

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/29/17.
 *
 * Provide "make" methods to create instances of [GoogleService]
 * and its related dependencies, such as OkHttpClient, Gson, etc.
 */
object GoogleServiceFactory : BaseServiceFactory() {

    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    /**
     * get instance API Google services
     */
    fun getGoogleService(): GoogleService {
        val okHttpClient = makeOkHttpClient(makeLoggingInterceptor())
        return makeGoogleService(okHttpClient, makeGson())
    }

    private fun makeGoogleService(okHttpClient: OkHttpClient, gson: Gson): GoogleService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        return retrofit.create(GoogleService::class.java)
    }
}
