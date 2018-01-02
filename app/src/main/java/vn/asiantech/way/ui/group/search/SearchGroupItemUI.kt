package vn.asiantech.way.ui.group.search

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 AsianTech inc.
 * @Author doan.bien@asiantech.vn on 12/26/17.
 */
class SearchGroupItemUI : AnkoComponent<ViewGroup> {
    internal lateinit var tvJoinGroup: TextView
    internal lateinit var tvNameUser: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        relativeLayout {
            lparams(matchParent, wrapContent)
            backgroundResource = R.color.colorWhite

            view {
                id = R.id.group_search_adapter_view_break_line
                backgroundResource = R.color.colorSearchScreenBackground
            }.lparams(matchParent, dip(1)) {
                leftMargin = dimen(R.dimen.search_group_left_margin)
                verticalMargin = dimen(R.dimen.search_group_adapter_vertical_margin)
            }

            linearLayout {
                gravity = Gravity.CENTER_VERTICAL

                tvNameUser = textView {
                    id = R.id.group_search_adapter_tv_name_user
                    padding = dimen(R.dimen.search_group_padding)
                    typeface = Typeface.DEFAULT_BOLD
                    textSize = px2dip(dimen(R.dimen.search_screen_text_size))
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }.lparams(dip(0), wrapContent) {
                    weight = 1f
                }

                tvJoinGroup = textView(R.string.join) {
                    id = R.id.group_search_adapter_tv_join_group
                    textColor = ContextCompat.getColor(context, R.color.colorWhite)
                    padding = dimen(R.dimen.search_group_padding)
                    backgroundResource = R.color.colorPinkLight
                    textSize = px2dip(dimen(R.dimen.group_text_size_normal))
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }.lparams {
                    rightMargin = dimen(R.dimen.search_group_padding)
                }

            }.lparams(matchParent, wrapContent) {
                below(R.id.group_search_adapter_view_break_line)
            }
        }
    }
}
