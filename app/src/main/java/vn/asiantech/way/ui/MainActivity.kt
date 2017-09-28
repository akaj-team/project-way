package vn.asiantech.way.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.register.RegisterActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
