package vn.asiantech.way.ui.searchlocation

import android.os.AsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */

class SearchLocationAsyncTask(private val mListener: SearchLocationListener)
    : AsyncTask<String, Void, List<MyLocation>>() {

    companion object {
        private const val API_KEY = "AIzaSyAIue0sTuwo7Qsqwi5hhx6zbncDaS2YxDY"
    }

    override fun doInBackground(vararg p0: String?): List<MyLocation>? {
        var check = true
        var myLocations: List<MyLocation>? = null
        val apiService = RetrofitClient.getAPIService()
        val query = p0[0]
        if (query != null) {
            apiService.getLocation(p0[0]!!, API_KEY)
                    .enqueue(object : Callback<APIResult> {
                        override fun onResponse(call: Call<APIResult>?, response: Response<APIResult>?) {
                            myLocations = response?.body()?.results
                            check = false
                        }

                        override fun onFailure(call: Call<APIResult>?, t: Throwable?) {
                            check = false
                        }

                    })
            while (check) {
                // Nothing here!
            }
        }
        return myLocations
    }

    override fun onPostExecute(result: List<MyLocation>?) {
        super.onPostExecute(result)
        mListener.onCompleted(result)
    }

    /**
     *  listener for Search Location AsyncTask
     */
    interface SearchLocationListener {

        /**
         *  event for AsyncTask completed
         */
        fun onCompleted(myLocations: List<MyLocation>?)
    }
}
