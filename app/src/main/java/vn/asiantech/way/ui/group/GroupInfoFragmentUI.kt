package vn.asiantech.way.ui.group

import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewManager
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragmentUI : AnkoComponent<GroupInfoFragment> {
    companion object {
        private const val ID_IMG_INVITE = 1001
        private const val MAX_LINES = 1
    }

    internal lateinit var tvGroupName: TextView
    internal lateinit var tvMembersCount: TextView
    internal lateinit var tvCreateAt: TextView
    internal lateinit var imgInvite: ImageView
    internal lateinit var imgLeave: ImageView
    internal lateinit var recyclerViewMember: RecyclerView

    override fun createView(ui: AnkoContext<GroupInfoFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)

            tvGroupName = textView {
                backgroundResource = R.color.colorBlueLight
                gravity = Gravity.CENTER_VERTICAL
                maxLines = MAX_LINES
                textColor = Color.WHITE
                textSize = px2dip(dimen(R.dimen.text_size_normal))
                val padding = dimen(R.dimen.group_screen_group_name_padding)
                leftPadding = padding
                rightPadding = padding
            }.lparams(matchParent, dimen(R.dimen.toolbar_height))

            tvMembersCount = textViewInfo()
            tvCreateAt = textViewInfo()
            textViewInfo(resources.getString(R.string.members_list))

            relativeLayout {
                lparams(matchParent, wrapContent)
                padding = dimen(R.dimen.group_screen_tv_count_padding_left)

                imgInvite = imageView(R.drawable.ic_person_add_deep_purple_a200_36dp) {
                    id = ID_IMG_INVITE
                }

                imgLeave = imageView(R.drawable.ic_exit_to_app_deep_purple_a200_36dp)
                        .lparams {
                            leftMargin = dimen(R.dimen.group_screen_img_leave_margin)
                            rightOf(ID_IMG_INVITE)
                        }
            }

            swipeRefreshLayout {
                recyclerViewMember = recyclerView {
                    lparams(matchParent, matchParent)
                    layoutManager = LinearLayoutManager(context)
                    backgroundResource = android.R.color.darker_gray
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
