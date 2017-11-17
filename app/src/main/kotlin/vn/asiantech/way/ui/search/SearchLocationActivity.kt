package vn.asiantech.way.ui.search

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.activity_search_location.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.asiantech.way.R
import vn.asiantech.way.data.model.search.MyLocation
import vn.asiantech.way.data.model.search.ResultPlaceDetail
import vn.asiantech.way.data.remote.APIUtil
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.share.ShareLocationActivity
import vn.asiantech.way.utils.AppConstants

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */
class SearchLocationActivity : BaseActivity() {

    companion object {
        private const val KEY_HISTORY = "history"
        private const val HISTORY_MAX_SIZE = 10
    }

    private var mTask: SearchLocationAsyncTask? = null
    private var mAdapter: LocationsAdapter? = null
    private var mMyLocations: MutableList<MyLocation> = mutableListOf()
    private var mIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)
        mIntent = Intent(applicationContext, ShareLocationActivity::class.java)
        initAdapter()
        locationSearch()
        onClick()
    }

    private fun onClick() {
        imgBtnBack.setOnClickListener {
            this.finish()
        }

        rlYourLocation.setOnClickListener {
            mIntent?.putExtra(AppConstants.KEY_CONFIRM, AppConstants.KEY_CURRENT_LOCATION)
            startActivity(mIntent)
        }

        rlChooseOnMap.setOnClickListener {
            mIntent?.putExtra(AppConstants.KEY_CONFIRM, AppConstants.KEY_CONFIRM)
            startActivity(mIntent)
        }

    }

    private fun locationSearch() {
        edtLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mMyLocations.clear()
                mAdapter?.notifyDataSetChanged()
                if (p0.toString().isNotEmpty()) {
                    if (mTask != null) {
                        mTask = null
                    }
                    mTask = SearchLocationAsyncTask(
                            object : SearchLocationAsyncTask.SearchLocationListener {
                                override fun onCompleted(myLocations: List<MyLocation>) {
                            val thread = Thread({
                                runOnUiThread({
                                    myLocations.forEach {
                                        mMyLocations.add(it)
                                    }
                                    mAdapter?.notifyDataSetChanged()
                                })
                            })
                            thread.start()
                        }
                    })
                    mTask?.execute(p0.toString())
                } else {
                    val history = getSearchHistory()
                    if (history != null) {
                        mMyLocations.addAll(history)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        })
    }

    private fun initAdapter() {
        val history = getSearchHistory()
        if (history != null) {
            mMyLocations.addAll(history)
        }
        mAdapter = LocationsAdapter(mMyLocations,
                object : LocationsAdapter.RecyclerViewOnItemClickListener {
            override fun onItemClick(myLocation: MyLocation) {
                if (myLocation.isHistory != null && myLocation.isHistory == true) {
                    saveSearchHistory(myLocation)
                    val bundle = Bundle()
                    bundle.putParcelable(AppConstants.KEY_LOCATION, myLocation)
                    bundle.putString(AppConstants.KEY_CONFIRM, AppConstants.KEY_SHARING)
                    mIntent?.putExtras(bundle)
                    startActivity(mIntent)
                    return
                }
                val progressDialog = ProgressDialog(this@SearchLocationActivity)
                progressDialog.setTitle(R.string.processing)
                progressDialog.isIndeterminate = true
                progressDialog.show()
                APIUtil.getService()?.getLocationDetail(myLocation.placeId,
                        AppConstants.GOOGLE_MAP_API_KEY)
                        ?.enqueue(object : Callback<ResultPlaceDetail> {
                            override fun onFailure(call: Call<ResultPlaceDetail>?, t: Throwable?) {
                                progressDialog.dismiss()
                                Toast.makeText(this@SearchLocationActivity,
                                        R.string.get_detail_error, Toast.LENGTH_LONG)
                                        .show()
                            }

                            override fun onResponse(call: Call<ResultPlaceDetail>?,
                                                    response: Response<ResultPlaceDetail>?) {
                                val resultLocation = response?.body()?.result
                                progressDialog.dismiss()
                                if (resultLocation != null) {
                                    saveSearchHistory(resultLocation)
                                    val bundle = Bundle()
                                    bundle.putParcelable(AppConstants.KEY_LOCATION, resultLocation)
                                    bundle.putString(AppConstants.KEY_CONFIRM,
                                            AppConstants.KEY_SHARING)
                                    mIntent?.putExtras(bundle)
                                    startActivity(mIntent)
                                } else {
                                    Toast.makeText(this@SearchLocationActivity,
                                            R.string.get_detail_error, Toast.LENGTH_LONG)
                                            .show()
                                }
                            }
                        })
            }
        })
        recyclerViewLocations.layoutManager = LinearLayoutManager(this)
        recyclerViewLocations.adapter = mAdapter
    }

    private fun getSearchHistory(): MutableList<MyLocation>? {
        val gson = Gson()
        val result = mutableListOf<MyLocation>()
        return try {
            val history = mSharedPreferences.getString(KEY_HISTORY, "[]")
            val jsonArray = JSONArray(history)
            (0 until jsonArray.length())
                    .mapTo(result) {
                        gson.fromJson(jsonArray.getJSONObject(it).toString()
                                , MyLocation::class.java)
                    }
            result
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    private fun saveSearchHistory(myLocation: MyLocation) {
        val gson = Gson()
        val editor = mSharedPreferences.edit()
        var history = getSearchHistory()
        if (history == null) {
            history = mutableListOf()
        }
        history = history.filter {
            it.id != myLocation.id
        }.toMutableList()
        myLocation.isHistory = true
        history.add(0, myLocation)
        if (history.size > HISTORY_MAX_SIZE) {
            history.removeAt(HISTORY_MAX_SIZE - 1)
        }
        editor?.putString(KEY_HISTORY, gson.toJson(history))
        editor?.apply()
    }
}
