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
        internal const val ID_LL_HEADER_LAYOUT = 2202
        internal const val ID_IMG_BTN_BACK = 2203
        internal const val ID_IMG_YOUR_LOCATION_ICON = 2204
        internal const val ID_IMG_CHOOSE_ON_MAP_ICON = 2205
        internal const val ID_RECYCLER_VIEW_LOCATIONS = 2206

        private const val DEFAULT_MARGIN_PADDING = 10
        private const val SMALL_MARGIN_PADDING = 5
        private const val BREAK_LINE_VIEW_HEIGHT = 0.5f
        private const val BREAK_LINE_LEFT_MARGIN = 64
        private const val BREAK_LINE_TOP_BOT_MARGIN = 2
    }

    internal lateinit var edtLocation: EditText

    override fun createView(ui: AnkoContext<SearchActivity>) = with(ui) {
        frameLayout {
            lparams {
                width = matchParent
                height = matchParent
                backgroundResource = R.color.colorSearchScreenBackground
            }

            imageView {
                imageResource = R.drawable.ic_path_icon
            }.lparams {
                width = wrapContent
                height = wrapContent
                gravity = Gravity.CENTER
            }

            relativeLayout {
                linearLayout {
                    id = ID_LL_HEADER_LAYOUT

                    imageButton {
                        id = ID_IMG_BTN_BACK
                        backgroundResource = R.drawable.ic_back_icon_button
                        onClick {
                            owner.onBackPressed()
                        }
                    }.lparams(wrapContent, wrapContent)

                    edtLocation = editText {
                        padding = dip(DEFAULT_MARGIN_PADDING)
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
                        leftMargin = dip(DEFAULT_MARGIN_PADDING)
                    }
                }.lparams(matchParent, wrapContent)

                verticalLayout {

                    relativeLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundColor = Color.WHITE
                        onClick {
                            owner.getCurrentLocation()
                        }

                        imageView {
                            id = ID_IMG_YOUR_LOCATION_ICON
                            backgroundResource = R.drawable.ic_my_location
                        }.lparams {
                            width = wrapContent
                            height = wrapContent
                            margin = dip(DEFAULT_MARGIN_PADDING)
                        }

                        textView {
                            padding = dip(SMALL_MARGIN_PADDING)
                            textSizeDimen = R.dimen.search_screen_text_size
                            textResource = R.string.your_location
                        }.lparams {
                            width = matchParent
                            height = wrapContent
                            margin = dip(DEFAULT_MARGIN_PADDING)
                            rightOf(ID_IMG_YOUR_LOCATION_ICON)
                        }

                        view {
                            backgroundResource = R.color.colorSearchScreenBackground
                        }.lparams {
                            width = matchParent
                            height = dip(BREAK_LINE_VIEW_HEIGHT)
                            leftMargin = dip(BREAK_LINE_LEFT_MARGIN)
                            topMargin = dip(BREAK_LINE_TOP_BOT_MARGIN)
                            bottomMargin = dip(BREAK_LINE_TOP_BOT_MARGIN)
                            below(ID_IMG_YOUR_LOCATION_ICON)
                        }
                    }.lparams(matchParent, wrapContent)

                    relativeLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundColor = Color.WHITE
                        onClick {
                            owner.chooseOnMap()
                        }

                        imageView {
                            id = ID_IMG_CHOOSE_ON_MAP_ICON
                            backgroundResource = R.drawable.ic_choose_on_map
                        }.lparams {
                            width = wrapContent
                            height = wrapContent
                            margin = dip(DEFAULT_MARGIN_PADDING)
                        }

                        textView {
                            padding = dip(SMALL_MARGIN_PADDING)
                            textSizeDimen = R.dimen.search_screen_text_size
                            textResource = R.string.choose_on_map
                        }.lparams {
                            width = matchParent
                            height = wrapContent
                            margin = dip(DEFAULT_MARGIN_PADDING)
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
                    topMargin = dip(DEFAULT_MARGIN_PADDING)
                    below(ID_LL_HEADER_LAYOUT)
                }
            }.lparams {
                width = matchParent
                height = matchParent
                padding = dip(DEFAULT_MARGIN_PADDING)
            }
        }
    }

    /**
     *
     */
    interface TextChangeListener : TextWatcher {
        override fun beforeTextChanged(var1: CharSequence, var2: Int, var3: Int, var4: Int) = Unit

        override fun onTextChanged(var1: CharSequence, var2: Int, var3: Int, var4: Int) = Unit

        override fun afterTextChanged(editable: Editable)
    }
}
