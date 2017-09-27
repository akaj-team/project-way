package vn.asiantech.way.ui.searchlocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_search_location.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity

/**
 * SearchLocationActivity.
 *
 * @author CuongCV
 */
class SearchLocationActivity : BaseActivity() {

    private var mTask: SearchLocationAsyncTask? = null
    private var mAdapter: LocationsAdapter? = null
    private var mMyLocations: MutableList<MyLocation> = mutableListOf()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)
        locationSearch()

        mAdapter = LocationsAdapter(mMyLocations, object : LocationsAdapter.RecyclerViewOnItemClickListener {
            override fun onItemClick(myLocation: MyLocation) {
                // TODO: Call to ShareLocationActivity

            }

        })
        recyclerViewLocations.layoutManager = LinearLayoutManager(this)
        recyclerViewLocations.adapter = mAdapter
        onClick()
    }

    private fun onClick() {
        imgBtnBack.setOnClickListener {
            this.finish()
        }

        rlYourLocation.setOnClickListener {
            // TODO: Call to ShareLocationActivity
        }

        rlChooseOnMap.setOnClickListener {
            // TODO: Call to ShareLocationActivity
        }

    }

    private fun locationSearch() {
        edtLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length > 0) {
                    if (mTask != null) {
                        mTask = null
                        mMyLocations.clear()
                        mAdapter?.notifyDataSetChanged()
                    }
                    mTask = SearchLocationAsyncTask(object : SearchLocationAsyncTask.SearchLocationListener {
                        override fun onCompleted(myLocations: List<MyLocation>?) {
                            val thread = Thread({
                                runOnUiThread({
                                    myLocations?.forEach {
                                        mMyLocations.add(it)
                                        mAdapter?.notifyItemInserted(mMyLocations.size - 1)
                                    }
                                })
                            })
                            thread.start()
                        }

                        override fun onStarted() {
                            Log.i("tag11", "started")
                        }

                    })
                    mTask?.execute(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        })
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(): Location? {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
        val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (gpsLocation.time > netLocation.time) {
            return gpsLocation
        } else {
            return netLocation
        }
    }
}
