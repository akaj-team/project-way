package vn.asiantech.way.ui.test

import android.os.Bundle
import org.jetbrains.anko.setContentView
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 15/12/2017
 */
class TestActivity : BaseActivity() {

    private val ui = TextSearchActivityUI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_text_search)
//    }

    override fun onBindViewModel() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}