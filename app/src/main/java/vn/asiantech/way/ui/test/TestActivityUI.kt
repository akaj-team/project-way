package vn.asiantech.way.ui.test

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.collapsingToolbarLayout
import org.jetbrains.anko.design.coordinatorLayout
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 15/12/2017
 */
class TestActivityUI : AnkoComponent<TestActivity> {

    companion object {
        const val WEIGHT = 1f
    }

    private lateinit var appBarLayout: AppBarLayout
    lateinit var toolbar: Toolbar

    override fun createView(ui: AnkoContext<TestActivity>) = with(ui) {
        coordinatorLayout {
            lparams(matchParent, matchParent)
            fitsSystemWindows = true

            appBarLayout = appBarLayout {
                fitsSystemWindows = true
//                app:toolbarId="@+id/toolbar">

                collapsingToolbarLayout {
                    fitsSystemWindows = true

                    verticalLayout {
                        gravity = Gravity.CENTER_VERTICAL

                        imageView(R.mipmap.ic_launcher_foreground)
                                .lparams(dip(150), dip(150))

                        textView(R.string.confirm)
                    }.lparams(matchParent, wrapContent) {
                        gravity = Gravity.CENTER_VERTICAL
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                    }

                    toolbar = toolbar {
                        id = R.id.toolbar
                        setContentInsetsAbsolute(0, 0)
                        linearLayout {
                            imageView(R.drawable.ic_keyboard_backspace_black_36dp)

                            textView(R.string.coming_soon) {
                                gravity = Gravity.CENTER
                            }.lparams(dip(0), matchParent) {
                                weight = WEIGHT
                                gravity = Gravity.CENTER
                            }

                            imageView(R.drawable.ic_settings_black_36dp)
                        }.lparams(matchParent, matchParent)
                    }.lparams(matchParent, dip(64)) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                    }
                }.lparams(matchParent, dip(180)) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }

            }.lparams(matchParent, wrapContent)

            scrollView {
                verticalLayout {
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                    textView("asdjashdjkashdjkashkjdhasd" +
                            "asdkaskldjasd" +
                            "askdjasjd" +
                            "jkashdsad") {
                        textSize = 100f
                    }
                }
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}