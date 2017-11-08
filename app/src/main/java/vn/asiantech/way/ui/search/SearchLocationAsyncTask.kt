package vn.asiantech.way.ui.search

import android.os.AsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.data.model.search.AutoCompleteResult
import vn.asiantech.way.data.model.search.MyLocation
import vn.asiantech.way.data.remote.APIUtil

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */
class SearchLocationAsyncTask(private val mAPIKey: String,
                              private val mListener: SearchLocationListener)
    : AsyncTask<String, Void, List<MyLocation>>() {

    override fun doInBackground(vararg p0: String?): List<MyLocation> {
        var check = true
        val myLocations = mutableListOf<MyLocation>()
        val query = p0[0]
        if (query != null) {
            APIUtil.getService()?.searchLocations(query,
                    mAPIKey)
                    ?.enqueue(object : Callback<AutoCompleteResult> {
                        override fun onFailure(call: Call<AutoCompleteResult>?, t: Throwable?) {
                            check = false
                        }

                        override fun onResponse(call: Call<AutoCompleteResult>?, response: Response<AutoCompleteResult>?) {
                            response?.body()?.predictions?.forEach {
                                myLocations.add(MyLocation(it.id, it.placeId, it.structuredFormatting.mainText, it.description))
                            }
                            check = false
                        }

                    })
            while (check) {
                // Nothing here!
            }
        }
        return myLocations
    }

    override fun onPostExecute(result: List<MyLocation>) {
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
        fun onCompleted(myLocations: List<MyLocation>)
    }
}
