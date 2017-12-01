package vn.asiantech.way.ui.search

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import org.json.JSONArray
import vn.asiantech.way.R
import vn.asiantech.way.data.model.AutoCompleteLocation
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.share.ShareActivity
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 27/11/2017
 */
class SearchActivity : BaseActivity() {

    private lateinit var ui: SearchActivityUI
    private val searchViewModel = SearchViewModel()
    private var locations = mutableListOf<WayLocation>()
    private val searchObservable = PublishSubject.create<String>()
    private lateinit var startShareActivityIntent: Intent
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = SearchActivityUI(locations)
        ui.setContentView(this)
        startShareActivityIntent = Intent(this, ShareActivity::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage(getString(R.string.processing))
        loadSearchHistory()
    }

    override fun onBindViewModel() {
        addDisposables(searchObservable
                .observeOnUiThread()
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe({
                    searchViewModel
                            .searchLocation(it)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::updateRecyclerViewLocation)
                }),
                searchViewModel.progressBarStatus
                        .observeOnUiThread()
                        .subscribe(this::updateProgressBarStatus))
    }

    /**
     * Search location by name.
     */
    internal fun searchLocations(query: String) {
        searchObservable.onNext(query)
    }

    /**
     * Get current location.
     */
    internal fun getCurrentLocation() {
        startShareActivityIntent.action = AppConstants.ACTION_CURRENT_LOCATION
        startActivity(startShareActivityIntent)
    }

    /**
     * Choose location on map.
     */
    internal fun chooseOnMap() {
        startShareActivityIntent.action = AppConstants.ACTION_CHOOSE_ON_MAP
        startActivity(startShareActivityIntent)
    }

    /**
     * On item of  RecyclerView click.
     */
    internal fun onItemClick(location: WayLocation) {
        if (location.isHistory != null && location.isHistory == true) {
            startSharedActivity(location)
            return
        }
        val placeId = location.placeId
        if (placeId != null) {
            searchViewModel.getLocationDetail(placeId)
                    .subscribe(this::startSharedActivity, {
                        toast(getString(R.string.error_message))
                    })
        }
    }

    /**
     * Load search history from shared references.
     */
    internal fun loadSearchHistory() {
        locations.clear()
        val histories = getSearchHistory()
        if (histories != null) {
            locations.addAll(histories)
        }
        ui.locationAdapter.notifyDataSetChanged()
    }

    private fun updateRecyclerViewLocation(data: List<AutoCompleteLocation>) {
        locations.clear()
        data.forEach {
            with(it) {
                locations.add(WayLocation(id, placeId, description, structuredFormatting.mainText))
            }
        }
        ui.locationAdapter.notifyDataSetChanged()
    }

    private fun updateProgressBarStatus(isShow: Boolean) {
        if (isShow) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    private fun getSearchHistory(): List<WayLocation>? {
        val gson = Gson()
        val result = mutableListOf<WayLocation>()
        return try {
            val history = prefs.getString(AppConstants.KEY_SEARCH_SCREEN_WAY_LOCATION_HISTORY,
                    "[]")
            val jsonArray = JSONArray(history)
            (0 until jsonArray.length())
                    .mapTo(result) {
                        gson.fromJson(jsonArray.getJSONObject(it).toString()
                                , WayLocation::class.java)
                    }
            result.toList()
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    private fun saveSearchHistory(location: WayLocation) {
        val gson = Gson()
        val editor = prefs.edit()
        var history = getSearchHistory()
        if (history == null) {
            history = mutableListOf()
        }
        history = history.filter {
            it.id != location.id
        }.toMutableList()
        location.isHistory = true
        history.add(0, location)
        if (history.size > AppConstants.SEARCH_SCREEN_HISTORY_MAX_SIZE) {
            history.removeAt(AppConstants.SEARCH_SCREEN_HISTORY_MAX_SIZE - 1)
        }
        editor?.putString(AppConstants.KEY_SEARCH_SCREEN_WAY_LOCATION_HISTORY, gson.toJson(history))
        editor?.apply()
    }

    private fun startSharedActivity(location: WayLocation) {
        saveSearchHistory(location)
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.KEY_LOCATION, location)
        bundle.putString(AppConstants.KEY_CONFIRM,
                AppConstants.KEY_SHARING)
        startShareActivityIntent.action = AppConstants.ACTION_SEND_WAY_LOCATION
        startShareActivityIntent.putExtras(bundle)
        startActivity(startShareActivityIntent)
    }
}
