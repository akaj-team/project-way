package vn.asiantech.way.ui.group.request

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 05/12/2017.
 */
class ShowRequestFragmentUI : AnkoComponent<ShowRequestFragment> {
    override fun createView(ui: AnkoContext<ShowRequestFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            textView(R.string.group_screen_invite_request) {
                backgroundResource = R.color.border_tracking_experience
                gravity = Gravity.CENTER_VERTICAL
                textSize = px2dip(dimen(R.dimen.text_size_normal))
                typeface = Typeface.DEFAULT_BOLD
                padding = dimen(R.dimen.group_screen_tv_count_padding_left)
            }.lparams(matchParent, dimen(R.dimen.toolbar_height))

            recyclerView {
                lparams(matchParent, matchParent)
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}
