package vn.asiantech.way.ui.share

import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.SupportMapFragment
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.utils.AppConstants

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 27/11/2017.
 */
class ShareActivity : BaseActivity() {

    private lateinit var shareActivityUI: ShareActivityUI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareActivityUI = ShareActivityUI()
        shareActivityUI.setContentView(this)
        when (intent.action) {
            AppConstants.ACTION_CURRENT_LOCATION -> {
                // TODO: 01/12/2017
                // Get current location and do more...
                Log.i("tag11", "1111111111111")
            }

            AppConstants.ACTION_CHOOSE_ON_MAP -> {
                // TODO: 01/12/2017
                // Choose location on the map and do more...
                Log.i("tag11", "2222222222")
            }

            AppConstants.ACTION_SEND_WAY_LOCATION -> {
                val location = intent.extras.getParcelable<WayLocation>(AppConstants.KEY_LOCATION)
                // TODO: 01/12/2017
                // Do more...
                Log.i("tag11", location.formatAddress)
            }

            else -> {
                // TODO: 01/12/2017
                // This is tracking case
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.share_activity_map, SupportMapFragment()).commit()
    }

    override fun onBindViewModel() {

    }

}
