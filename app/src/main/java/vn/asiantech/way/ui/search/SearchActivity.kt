package vn.asiantech.way.ui.search

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.AutoCompleteLocation
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.extension.toast
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.share.ShareActivity
import vn.asiantech.way.utils.AppConstants
import vn.asiantech.way.utils.SharedPreferencesUtil
import java.util.concurrent.TimeUnit

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 27/11/2017
 */
class SearchActivity : BaseActivity() {

    private lateinit var ui: SearchActivityUI
    private lateinit var searchViewModel: SearchViewModel
    private var locations = mutableListOf<WayLocation>()
    private val searchObservable = PublishSubject.create<String>()
    private lateinit var startShareActivityIntent: Intent
    private lateinit var progressDialog: ProgressDialog
    private lateinit var prefs: SharedPreferencesUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = SearchActivityUI(locations)
        ui.setContentView(this)
        prefs = SharedPreferencesUtil(this)
        startShareActivityIntent = Intent(this, ShareActivity::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage(getString(R.string.processing))
        searchViewModel = SearchViewModel(this)
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
                        .subscribe(this::updateProgressBarStatus),

                searchViewModel.loadSearchHistories()
                        .observeOnUiThread()
                        .subscribe(this::onLoadSearchHistoriesComplete))
    }

    internal fun loadSearchHistory() {
        addDisposables(searchViewModel.loadSearchHistories()
                .observeOnUiThread()
                .subscribe(this::onLoadSearchHistoriesComplete))
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
    private fun onLoadSearchHistoriesComplete(histories: List<WayLocation>?) {
        locations.clear()
        if (histories != null) {
            locations.addAll(histories)
        }
        d("xxx", locations.size.toString())
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

    private fun startSharedActivity(location: WayLocation) {
        searchViewModel.saveSearchHistories(location)
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.KEY_LOCATION, location)
        startShareActivityIntent.action = AppConstants.ACTION_SEND_WAY_LOCATION
        startShareActivityIntent.putExtras(bundle)
        startActivity(startShareActivityIntent)
    }
}
