package vn.asiantech.way.ui.searchlocation

import android.os.AsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * SearchLocationAsyncTask.
 *is
 * @author CuongCV
 */
class SearchLocationAsyncTask(val mListener: SearchLocationListener) : AsyncTask<String, Void, List<MyLocation>>() {

    override fun doInBackground(vararg p0: String?): List<MyLocation>? {
        mListener.onStarted()
        var check = true
        var myLocations: List<MyLocation>? = null
        val apiService = RetrofitClient.getAPIService()
        apiService.getLocation(p0.get(0)!!, "AIzaSyAIue0sTuwo7Qsqwi5hhx6zbncDaS2YxDY")
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

        }
        return myLocations
    }

    override fun onPostExecute(result: List<MyLocation>?) {
        super.onPostExecute(result)
        mListener.onCompleted(result)
    }

    interface SearchLocationListener {
        fun onCompleted(myLocations: List<MyLocation>?)
        fun onStarted()
    }
}
