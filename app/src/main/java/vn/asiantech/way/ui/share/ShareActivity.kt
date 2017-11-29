package vn.asiantech.way.ui.share

import android.os.Bundle
import com.google.android.gms.maps.SupportMapFragment
import org.jetbrains.anko.setContentView
import vn.asiantech.way.ui.base.BaseActivity

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
        supportFragmentManager.beginTransaction().replace(ShareActivityUI.ID_MAP, SupportMapFragment()).commit()
    }
}
