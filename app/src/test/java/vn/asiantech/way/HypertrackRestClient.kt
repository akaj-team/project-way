package vn.asiantech.way

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.asiantech.way.data.source.remote.hypertrackapi.CustomHyperTrackCallAdapterFactory

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 18/12/2017
 */
class HypertrackRestClient {
    companion object {
        private const val TOKEN = "token sk_test_3b4f98fbf6b58eb9d6f710c98c7fcb7a52d2acb6"

        fun <T> getClient(baseUrl: HttpUrl, clazz: Class<T>): T {
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
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CustomHyperTrackCallAdapterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(clazz)
        }
    }
}
