package vn.asiantech.way.ui.test

import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 15/12/2017
 */
class TextSearchActivityUI : AnkoComponent<TestActivity> {

    companion object {
        const val WEIGHT = 1f
    }

    private lateinit var appBarLayout: AppBarLayout
    lateinit var toolbar: Toolbar

    override fun createView(ui: AnkoContext<TestActivity>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            padding = dimen(R.dimen.text_search_padding)

            linearLayout {
                linearLayout {
                    backgroundResource = R.drawable.bg_search_view_text_search_activity

                    editText {
                        id = R.id.text_search_edt_query
                    }.lparams(0, wrapContent){

                    }
                }.lparams(0, wrapContent) {
                    weight = 1f
                }
            }.lparams(matchParent, wrapContent)
        }.apply {
            gravity = Gravity.CENTER_VERTICAL
        }
    }
}