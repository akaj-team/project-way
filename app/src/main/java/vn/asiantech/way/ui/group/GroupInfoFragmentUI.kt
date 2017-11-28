package vn.asiantech.way.ui.group

import android.support.v4.app.ActivityCompat
import android.view.Gravity
import android.view.ViewManager
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import vn.asiantech.way.R

/**
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragmentUI : AnkoComponent<GroupInfoFragment> {
    companion object {
        private const val ID_IMG_INVITE = 1001
        private const val MAX_LINES = 1
    }

    override fun createView(ui: AnkoContext<GroupInfoFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)

            textView {
                backgroundColor = ActivityCompat.getColor(context, R.color.colorBlueLight)
                gravity = Gravity.CENTER_VERTICAL
                maxLines = MAX_LINES
                textColor = ActivityCompat.getColor(context, R.color.white)
                textSize = px2dip(dimen(R.dimen.text_size_normal))
                val padding = dimen(R.dimen.group_screen_group_name_padding)
                leftPadding = padding
                rightPadding = padding
            }.lparams(matchParent, dimen(R.dimen.toolbar_height))

            textViewInfo()
            textViewInfo()
            textViewInfo(resources.getString(R.string.members_list))

            relativeLayout {
                lparams(matchParent, wrapContent)
                padding = dip(10)

                imageView {
                    id = ID_IMG_INVITE
                    backgroundResource = R.drawable.ic_person_add_deep_purple_a200_36dp
                }

                imageView {
                    backgroundResource = R.drawable.ic_exit_to_app_deep_purple_a200_36dp
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_img_leave_margin)
                    rightOf(ID_IMG_INVITE)
                }
            }

            swipeRefreshLayout {
                recyclerView {
                    lparams(matchParent, matchParent)
                    backgroundColor = ActivityCompat.getColor(context, android.R.color.darker_gray)
                    padding = dimen(R.dimen.group_screen_recycler_view_padding)
                }
            }.lparams(matchParent, matchParent)
        }
    }

    private fun ViewManager.textViewInfo(str: String = "") = textView {
        text = str
        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
    }.apply {
        val paddingTop = dimen(R.dimen.group_screen_recycler_view_padding)
        val paddingRight = dimen(R.dimen.group_screen_tv_count_padding_left)
        bottomPadding = paddingTop
        rightPadding = paddingRight
        leftPadding = paddingRight
        topPadding = paddingTop
    }
}
