package vn.asiantech.way.ui.share

import android.os.Bundle
import com.google.android.gms.maps.SupportMapFragment
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.model.WayLocation
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 27/11/2017.
 */
class ShareActivity : BaseActivity() {

    companion object {
        const val ACTION_SEND_WAY_LOCATION = "action_send_way_location"
        const val KEY_LOCATION = "location"
        const val ACTION_CHOOSE_ON_MAP = "action_choose_on_map"
        const val ACTION_CURRENT_LOCATION = "action_current_location"
    }

    private lateinit var shareActivityUI: ShareActivityUI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareActivityUI = ShareActivityUI()
        shareActivityUI.setContentView(this)
        when (intent.action) {
            ACTION_CURRENT_LOCATION -> {
                // TODO: 01/12/2017
                // Get current location and do more...
            }

            ACTION_CHOOSE_ON_MAP -> {
                // TODO: 01/12/2017
                // Choose location on the map and do more...
            }

            ACTION_SEND_WAY_LOCATION -> {
                val location = intent.extras.getParcelable<WayLocation>(KEY_LOCATION)
                // TODO: 01/12/2017
                // Do more...
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
