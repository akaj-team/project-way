package vn.asiantech.way.ui.search

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 27/11/2017
 */
class SearchActivityUI(private val locationAdapter: LocationAdapter)
    : AnkoComponent<SearchActivity> {

    companion object {
        internal const val ID_LL_HEADER_LAYOUT = 1001
        internal const val ID_IMG_YOUR_LOCATION_ICON = 1002
        internal const val ID_IMG_CHOOSE_ON_MAP_ICON = 1003
        internal const val ID_RECYCLER_VIEW_LOCATIONS = 1004
    }

    internal lateinit var edtLocation: EditText

    override fun createView(ui: AnkoContext<SearchActivity>) = with(ui) {
        frameLayout {
            lparams {
                width = matchParent
                height = matchParent
                backgroundResource = R.color.colorSearchScreenBackground
            }

            imageView(R.drawable.ic_path_icon).lparams {
                width = wrapContent
                height = wrapContent
                gravity = Gravity.CENTER
            }

            relativeLayout {
                linearLayout {
                    id = ID_LL_HEADER_LAYOUT

                    imageButton {
                        backgroundResource = R.drawable.ic_back_icon_button
                        onClick {
                            owner.onBackPressed()
                        }
                    }.lparams(wrapContent, wrapContent)

                    edtLocation = editText {
                        padding = dimen(R.dimen.default_padding_margin)
                        backgroundColor = Color.WHITE
                        singleLine = true
                        hintResource = R.string.search_location_enter_a_place
                        textSizeDimen = R.dimen.search_screen_text_size
                        addTextChangedListener(object : TextChangeListener {
                            override fun afterTextChanged(editable: Editable) {
                                if (editable.toString().trim().isNotEmpty()) {
                                    owner.searchLocations(editable.toString().trim())
                                }
                            }
                        })
                    }.lparams {
                        width = matchParent
                        height = matchParent
                        leftMargin = dimen(R.dimen.default_padding_margin)
                    }
                }.lparams(matchParent, wrapContent)

                verticalLayout {

                    relativeLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundColor = Color.WHITE
                        onClick {
                            owner.getCurrentLocation()
                        }

                        imageView(R.drawable.ic_my_location) {
                            id = ID_IMG_YOUR_LOCATION_ICON
                        }.lparams {
                            width = wrapContent
                            height = wrapContent
                            margin = dimen(R.dimen.default_padding_margin)
                        }

                        textView(R.string.your_location) {
                            padding = dimen(R.dimen.small_padding_margin)
                            textSizeDimen = R.dimen.search_screen_text_size
                        }.lparams {
                            width = matchParent
                            height = wrapContent
                            margin = dimen(R.dimen.default_padding_margin)
                            rightOf(ID_IMG_YOUR_LOCATION_ICON)
                        }

                        view {
                            backgroundResource = R.color.colorSearchScreenBackground
                        }.lparams {
                            width = matchParent
                            height = dimen(R.dimen.break_line_view_height)
                            leftMargin = dimen(R.dimen.break_line_left_margin)
                            topMargin = dimen(R.dimen.break_line_top_bot_margin)
                            bottomMargin = dimen(R.dimen.break_line_top_bot_margin)
                            below(ID_IMG_YOUR_LOCATION_ICON)
                        }
                    }.lparams(matchParent, wrapContent)

                    relativeLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundColor = Color.WHITE
                        onClick {
                            owner.chooseOnMap()
                        }

                        imageView(R.drawable.ic_choose_on_map) {
                            id = ID_IMG_CHOOSE_ON_MAP_ICON
                        }.lparams {
                            width = wrapContent
                            height = wrapContent
                            margin = dimen(R.dimen.default_padding_margin)
                        }

                        textView(R.string.choose_on_map) {
                            padding = dimen(R.dimen.small_padding_margin)
                            textSizeDimen = R.dimen.search_screen_text_size
                        }.lparams {
                            width = matchParent
                            height = wrapContent
                            margin = dimen(R.dimen.default_padding_margin)
                            rightOf(ID_IMG_CHOOSE_ON_MAP_ICON)
                        }
                    }.lparams(matchParent, wrapContent)

                    recyclerView {
                        id = ID_RECYCLER_VIEW_LOCATIONS
                        layoutManager = LinearLayoutManager(context)
                        adapter = locationAdapter
                    }.lparams(matchParent, matchParent)
                }.lparams {
                    width = matchParent
                    height = matchParent
                    topMargin = dimen(R.dimen.default_padding_margin)
                    below(ID_LL_HEADER_LAYOUT)
                }
            }.lparams {
                width = matchParent
                height = matchParent
                padding = dimen(R.dimen.default_padding_margin)
            }
        }
    }

    /**
     * This interface use to handle Text change event of edit text.
     */
    interface TextChangeListener : TextWatcher {
        override fun beforeTextChanged(var1: CharSequence, var2: Int, var3: Int, var4: Int) = Unit

        override fun onTextChanged(var1: CharSequence, var2: Int, var3: Int, var4: Int) = Unit

        override fun afterTextChanged(editable: Editable)
    }
}
