package vn.asiantech.way.ui.profileanko

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 12/15/17.
 */
class ProfileActivity : AppCompatActivity() {
    private lateinit var ui  : ProfileActivityUI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ProfileActivityUI(MyViewPagerAdapter(supportFragmentManager))
        ui.setContentView(this)
    }
}
