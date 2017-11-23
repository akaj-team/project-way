package vn.asiantech.way.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 23/11/2017.
 */

object RoadClient {
    private const val BASE_URL = "https://roads.googleapis.com/v1/"
    private var retrofit: Retrofit? = null
    private val builder = OkHttpClient.Builder()

    /**
     * get retrofit
     * @return Retrofit
     */
    private fun getClient(): Retrofit? {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
        }
        return retrofit
    }
    /**
     * function use get Service
     */
    internal fun getService(): RoadServiceApi? = RoadClient.getClient()?.create(RoadServiceApi::class.java)
}