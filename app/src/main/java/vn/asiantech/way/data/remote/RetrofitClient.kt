package vn.asiantech.way.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
class RetrofitClient {
    companion object {
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/"
        private var retrofit: Retrofit? = null
        private val builder = OkHttpClient.Builder()

        /**
         * get retrofit
         * @return Retrofit
         */
        internal fun getClient(): Retrofit? {
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
    }
}
