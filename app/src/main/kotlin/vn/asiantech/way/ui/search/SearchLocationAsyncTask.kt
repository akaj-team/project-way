package vn.asiantech.way.ui.search

import android.os.AsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.data.model.search.MyLocation
import vn.asiantech.way.data.model.search.ResultLocation
import vn.asiantech.way.data.remote.APIUtil

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
        val apiService = APIUtil.getService()
        val query = p0[0]
        if (query != null) {
            apiService?.getLocation(query, API_KEY)
                    ?.enqueue(object : Callback<ResultLocation> {
                        override fun onResponse(call: Call<ResultLocation>?, response: Response<ResultLocation>?) {
                            myLocations = response?.body()?.results
                            check = false
                        }

                        override fun onFailure(call: Call<ResultLocation>?, t: Throwable?) {
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
