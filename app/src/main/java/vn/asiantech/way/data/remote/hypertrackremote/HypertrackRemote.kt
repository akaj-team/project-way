package vn.asiantech.way.data.remote.hypertrackremote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 09/11/2017
 */
class HypertrackRemote {
    companion object {
        private const val BASE_URL = "https://api.hypertrack.com/api/v1/"
        private const val TOKEN = "token sk_test_3b4f98fbf6b58eb9d6f710c98c7fcb7a52d2acb6"
        private var instance: Retrofit? = null

        private fun getClient(): Retrofit {
            if (instance == null) {
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
                instance = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
            }
            return instance!!
        }

        /**
         * get Api service
         */
        fun getApiService(): HypertrackService {
            return getClient().create(HypertrackService::class.java)
        }
    }
}
