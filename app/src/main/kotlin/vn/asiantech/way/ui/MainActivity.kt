package vn.asiantech.way.ui

import android.os.Bundle
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}