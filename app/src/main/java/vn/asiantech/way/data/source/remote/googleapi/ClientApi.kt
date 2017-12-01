package vn.asiantech.way.data.source.remote.googleapi

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vn.asiantech.way.BuildConfig
import java.util.concurrent.TimeUnit

/**
 * Created by tien.hoang on 11/28/17.
 */
/**
 * Remote api
 */
object ApiClient {

    private val TIMEOUT = 20_000L //20 seconds

    val instance: ApiService by lazy {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        httpClient.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        httpClient.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        httpClient.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()))
                .client(httpClient.build())
                .build()

        retrofit.create(ApiService::class.java)
    }
}
