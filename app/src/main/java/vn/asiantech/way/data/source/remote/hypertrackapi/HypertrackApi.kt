package vn.asiantech.way.data.source.remote.hypertrackapi

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 14/11/2017
 */
object HypertrackApi {
    private const val BASE_URL = "https://api.hypertrack.com/api/v1/"
    private const val TOKEN = "token sk_test_3b4f98fbf6b58eb9d6f710c98c7fcb7a52d2acb6"

    //  private var instance: Retrofit? = null

    val instance: HypertrackService by lazy {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.interceptors().add(Interceptor { chain ->
            val original = chain.request()
            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                    .header("Authorization", TOKEN)
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        })

        val client = httpClientBuilder.build()
        val gson = GsonBuilder().serializeNulls().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        retrofit.create(HypertrackService::class.java)
    }
}
