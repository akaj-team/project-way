package vn.asiantech.way.ui

import android.content.Intent
import android.os.Bundle
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.search.SearchActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, SearchActivity::class.java))
    }
}
