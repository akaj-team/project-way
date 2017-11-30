package vn.asiantech.way.data.source.remote.hypertrackapi

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.asiantech.way.data.source.remote.core.BaseServiceFactory

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 14/11/2017
 */
object HyperTrackServiceFactory : BaseServiceFactory() {
    private const val BASE_URL = "https://api.hypertrack.com/api/v1/"
    private const val API_TOKEN = "sk_test_3b4f98fbf6b58eb9d6f710c98c7fcb7a52d2acb6"

    fun getHyperTrackService(): HyperTrackService {
        okHttpBuilder.addInterceptor(makeLoggingInterceptor())
        return makeHyperTrackService(okHttpBuilder.build(), makeGson())
    }

    private fun makeHyperTrackService(okHttpClient: OkHttpClient, gson: Gson): HyperTrackService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        return retrofit.create(HyperTrackService::class.java)
    }

    val okHttpBuilder: OkHttpClient.Builder
        get() {
            // Create http client
        val httpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val original = chain.request()

                    // Header
                    val request = original.newBuilder()
                            .header("Authorization",
                                    "token $API_TOKEN")
                            .method(original.method(), original.body())
                            .build()

                    return@Interceptor chain.proceed(request)
                })
            return httpClient
    }
}
