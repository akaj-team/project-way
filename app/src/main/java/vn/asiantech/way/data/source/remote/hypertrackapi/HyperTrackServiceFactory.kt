package vn.asiantech.way.data.source.remote.hypertrackapi

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 14/11/2017
 */
object HyperTrackServiceFactory {
    private const val BASE_URL = "https://api.hypertrack.com/api/v1/"
    private const val API_TOKEN = "sk_test_3b4f98fbf6b58eb9d6f710c98c7fcb7a52d2acb6"
    private const val TIME_OUT = 120L

    fun makeHyperTrackService(isDebug: Boolean): HyperTrackService {
        val okHttpClient = makeOkHttpClient(
                makeLoggingInterceptor(isDebug))



        return makeHyperTrackService(okHttpClient, makeGson())
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

    fun makeGson(): Gson {
        return GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        // create http client
        val httpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val original = chain.request()

                    //header
                    val request = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body())
                            .build()

                    return@Interceptor chain.proceed(request)
                })

        // log interceptor
        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        httpClient.addInterceptor(loggingInterceptor)
        return loggingInterceptor
    }

    fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build()
    }
}
