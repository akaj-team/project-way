package vn.asiantech.way.ui.test

import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.extension.onTextChangeListener

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 15/12/2017
 */
class TextSearchActivityUI : AnkoComponent<TestActivity> {

    companion object {
        const val WEIGHT = 1f
    }

    private lateinit var imgClearText: ImageView
    private lateinit var edtSearchQuery: EditText

    override fun createView(ui: AnkoContext<TestActivity>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            padding = dimen(R.dimen.text_search_padding)

            linearLayout {
                gravity = Gravity.CENTER_VERTICAL

                linearLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    backgroundResource = R.drawable.bg_search_view_text_search_activity

                    imageView(R.drawable.ic_search_grey_500_24dp)
                            .lparams {
                                leftMargin = dimen(R.dimen.text_search_padding)
                            }

                    edtSearchQuery = editText {
                        id = R.id.text_search_edt_query
                        hintResource = R.string.text_search_edit_text_hint
                        padding = dimen(R.dimen.text_search_padding)
                        singleLine = true
                        textSize = 14f
                        backgroundColorResource = R.color.colorTransparent
                        onTextChangeListener({}, {}, {
                            if (it != null) {
                                if (it.isNotEmpty()) {
                                    imgClearText.visibility = View.VISIBLE
                                } else {
                                    imgClearText.visibility = View.GONE
                                }
                            }
                        })
                    }.lparams(0, wrapContent) {
                        weight = 1f
                    }

                    imgClearText = imageView(R.drawable.ic_close_black_24dp) {
                        id = R.id.text_search_img_clear_text
                        visibility = View.GONE
                        onClick {
                            edtSearchQuery.text.clear()
                        }
                    }.lparams {
                        horizontalMargin = dimen(R.dimen.text_search_padding)
                    }
                }.lparams(0, wrapContent) {
                    weight = 1f
                }

                textView(R.string.text_search_cancel) {
                    id = R.id.text_search_tv_cancel
                }.lparams {
                    horizontalMargin = dimen(R.dimen.text_search_padding)
                }
            }.lparams(matchParent, wrapContent)

            frameLayout {
                id = R.id.text_search_fr_content

                recyclerView {
                    layoutManager = LinearLayoutManager(ctx)
                    adapter = RecentSearchAdapter()
                }
            }.lparams(matchParent, matchParent) {
                topMargin = dimen(R.dimen.text_search_padding)
            }
        }
    }
}
