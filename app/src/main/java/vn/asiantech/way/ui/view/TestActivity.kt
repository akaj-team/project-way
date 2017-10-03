package vn.asiantech.way.ui.view

import android.os.Bundle
import kotlinx.android.synthetic.main.test_layout.*
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Created by haingoq on 29/09/2017.
 */
class TestActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
    }
}
