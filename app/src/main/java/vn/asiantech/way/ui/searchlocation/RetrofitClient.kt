package vn.asiantech.way.ui.searchlocation

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */

class RetrofitClient {

    companion object {
        private var retrofit: Retrofit? = null
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/"

        private fun getClient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!
        }

        /**
         *  get api service
         */
        fun getAPIService(): APIService {
            return getClient().create(APIService::class.java)
        }

    }
}
