package vn.asiantech.way.ui.profileanko

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.ViewManager
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager
import vn.asiantech.way.R
import vn.asiantech.way.ui.profile.DummyFragment

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 12/15/17.
 */
class ProfileActivityUI(private val myViewPagerAdapter: MyViewPagerAdapter) : AnkoComponent<ProfileActivity> {
    private lateinit var collapseToolbarLayout: CollapsingToolbarLayout
    private lateinit var appBarLayout1: AppBarLayout
    internal lateinit var tabBarLayout: TabLayout
    internal lateinit var viewPager: ViewPager
    internal lateinit var toolBar: Toolbar


    companion object {
        private const val ID_VIEW_PAGER = 123
    }

    override fun createView(ui: AnkoContext<ProfileActivity>) = with(ui) {
        relativeLayout {
            coordinatorLayout {
                lparams(matchParent, matchParent)
                fitsSystemWindows = true
                appBarLayout1 = appBarLayout {
                    lparams(matchParent, wrapContent) {
                        scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
                    }
                    fitsSystemWindows = true
                    addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                        if (Math.abs(verticalOffset) - appBarLayout!!.totalScrollRange == 0) {
                            //  Collapsed
                            Log.d("VVVV", "Collapsed")
                        } else {
                            //Expanded
                            Log.d("VVVV", "Expanded")
                        }
                    }
                    collapseToolbarLayout = collapsingToolbarLayout {
                        fitsSystemWindows = true
                        isTitleEnabled = false
                        imageView(R.drawable.bg_luffy) {
                        }.lparams(matchParent, matchParent) {
                            bottomMargin = dimen(R.dimen.toolbar_height_2)
                            collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                        }
                        toolBar = myToolBar {
                        }.lparams(matchParent, dimen(R.dimen.toolbar_height)) {
                            gravity = Gravity.TOP
                            collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                        }
                        tabBarLayout = tabLayout {
                            backgroundResource = R.color.light_orange
                        }.lparams(matchParent, dimen(R.dimen.toolbar_height)) {
                            gravity = Gravity.BOTTOM
                        }
                    }.lparams(matchParent, dimen(R.dimen.height_256)) {
                        scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                    }
                    toolbar {

                    }
                }
                viewPager = viewPager {
                    id = ID_VIEW_PAGER
                    backgroundResource = R.color.accent_material_dark
                    myViewPagerAdapter.addFrag(DummyFragment(), "FIRST")
                    myViewPagerAdapter.addFrag(DummyFragment(), "SECOND")
                    myViewPagerAdapter.addFrag(DummyFragment(), "THRID")
                    adapter = myViewPagerAdapter
                }.lparams(matchParent, matchParent) {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                }
                tabBarLayout.setupWithViewPager(viewPager)
            }
        }
    }
}

private inline fun ViewManager.myToolBar(theme: Int = 0, init: Toolbar.() -> Unit): Toolbar =
        ankoView({ Toolbar(it) }, theme, init)
