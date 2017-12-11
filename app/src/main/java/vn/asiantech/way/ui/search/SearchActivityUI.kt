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
import vn.asiantech.way.data.model.WayLocation

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 27/11/2017
 */
class SearchActivityUI(private val locations: MutableList<WayLocation>)
    : AnkoComponent<SearchActivity> {

    internal lateinit var edtLocation: EditText
    internal val locationAdapter = LocationAdapter(locations)

    override fun createView(ui: AnkoContext<SearchActivity>) = with(ui) {
        frameLayout {
            lparams(matchParent, matchParent) {
                backgroundResource = R.color.colorSearchScreenBackground
            }

            imageView(R.drawable.ic_path_icon).lparams {
                gravity = Gravity.CENTER
            }

            relativeLayout {
                linearLayout {
                    id = R.id.search_activity_ui_ll_header_layout

                    imageButton {
                        backgroundResource = R.drawable.ic_back_icon_button
                        onClick {
                            owner.onBackPressed()
                        }
                    }

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
                                } else {
                                    owner.loadSearchHistory()
                                }
                            }
                        })
                    }.lparams(matchParent, matchParent) {
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
                            id = R.id.search_activity_ui_img_your_location_icon
                        }.lparams {
                            margin = dimen(R.dimen.default_padding_margin)
                        }

                        textView(R.string.your_location) {
                            padding = dimen(R.dimen.small_padding_margin)
                            textSizeDimen = R.dimen.search_screen_text_size
                        }.lparams(matchParent, wrapContent) {
                            margin = dimen(R.dimen.default_padding_margin)
                            rightOf(R.id.search_activity_ui_img_your_location_icon)
                        }

                        view {
                            backgroundResource = R.color.colorSearchScreenBackground
                        }.lparams(matchParent, dimen(R.dimen.break_line_view_height)) {
                            leftMargin = dimen(R.dimen.break_line_left_margin)
                            topMargin = dimen(R.dimen.break_line_top_bot_margin)
                            bottomMargin = dimen(R.dimen.break_line_top_bot_margin)
                            below(R.id.search_activity_ui_img_your_location_icon)
                        }
                    }.lparams(matchParent, wrapContent)

                    relativeLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundColor = Color.WHITE
                        onClick {
                            owner.chooseOnMap()
                        }

                        imageView(R.drawable.ic_choose_on_map) {
                            id = R.id.search_activity_ui_tv_choose_on_map_icon
                        }.lparams {
                            margin = dimen(R.dimen.default_padding_margin)
                        }

                        textView(R.string.choose_on_map) {
                            padding = dimen(R.dimen.small_padding_margin)
                            textSizeDimen = R.dimen.search_screen_text_size
                        }.lparams(matchParent, wrapContent) {
                            margin = dimen(R.dimen.default_padding_margin)
                            rightOf(R.id.search_activity_ui_tv_choose_on_map_icon)
                        }
                    }.lparams(matchParent, wrapContent)

                    recyclerView {
                        id = R.id.search_activity_ui_recycler_view_location
                        locationAdapter.onItemClick = {
                            owner.onItemClick(it)
                        }
                        layoutManager = LinearLayoutManager(context)
                        adapter = locationAdapter
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, matchParent) {
                    topMargin = dimen(R.dimen.default_padding_margin)
                    below(R.id.search_activity_ui_ll_header_layout)
                }
            }.lparams(matchParent, matchParent) {
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
