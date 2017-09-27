package vn.asiantech.way.ui.searchlocation

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClient.
 *
 * @author CuongCV
 */

class RetrofitClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl("https://maps.googleapis.com/maps/api/place/textsearch/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!
        }

        fun getAPIService(): APIService {
            return getClient().create(APIService::class.java)
        }

    }
}
