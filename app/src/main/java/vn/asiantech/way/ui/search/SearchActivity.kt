package vn.asiantech.way.ui.search

import android.os.Bundle
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import vn.asiantech.way.R
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 27/11/2017
 */
class SearchActivity : BaseActivity() {

    private lateinit var searchActivityUI: SearchActivityUI
    private lateinit var adapter: LocationAdapter
    private var locations = mutableListOf<WayLocation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = LocationAdapter(locations)
        searchActivityUI = SearchActivityUI(adapter)
        searchActivityUI.setContentView(this)
    }

    /**
     * Search location by name.
     */
    internal fun searchLocations(query: String) {
        // TODO: 28/11/2017
        // Init later.
    }

    /**
     * Get current location.
     */
    internal fun getCurrentLocation() {
        // Init later.
        toast(R.string.coming_soon)
    }

    /**
     * Choose location on map.
     */
    internal fun chooseOnMap() {
        // Init later.
        toast(R.string.coming_soon)
    }

    /**
     * On item of  RecyclerView click.
     */
    internal fun onItemClick(location: WayLocation) {
        // TODO: 28/11/2017
        // Dummy data
        toast(R.string.coming_soon)
    }
}
