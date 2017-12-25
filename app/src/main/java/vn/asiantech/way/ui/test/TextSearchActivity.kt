package vn.asiantech.way.ui.test

import android.os.Bundle
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.test.recent.RecentSearchFragment
import vn.asiantech.way.ui.test.tag.TagSearchFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 15/12/2017
 */
class TextSearchActivity : BaseActivity() {

    private val ui = TextSearchActivityUI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.setContentView(this)
        handleUpdateRecentSearch()
    }

    override fun onBindViewModel() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    internal fun handleUpdateRecentSearch() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.text_search_fr_content, RecentSearchFragment())
                .commit()
    }

    internal fun handleUpdateTagSearch() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.text_search_fr_content, TagSearchFragment())
                .commit()
    }
}