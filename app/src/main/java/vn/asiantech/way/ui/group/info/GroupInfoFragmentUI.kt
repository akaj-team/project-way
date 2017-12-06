package vn.asiantech.way.ui.group.info

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.ImageView
import android.widget.TextView
import com.hypertrack.lib.models.User
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragmentUI(userId: String, members: MutableList<User>)
    : AnkoComponent<GroupInfoFragment> {

    internal lateinit var tvGroupName: TextView
    internal lateinit var tvMembersCount: TextView
    internal lateinit var tvCreateAt: TextView
    internal lateinit var imgApprove: ImageView
    internal val memberListAdapter = MemberListAdapter(userId, members)

    override fun createView(ui: AnkoContext<GroupInfoFragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)

            tvGroupName = textView {
                backgroundResource = R.color.colorBlueLight
                gravity = Gravity.CENTER_VERTICAL
                maxLines = 1
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

                imageView(R.drawable.ic_person_add_deep_purple_a200_36dp) {
                    id = R.id.group_info_img_invite
                    onClick {
                        owner.callToInviteFragment()
                    }
                }

                imgApprove = imageView(R.drawable.ic_spellcheck_deep_purple_a200_36dp) {
                    id = R.id.group_info_img_approve
                    visibility = View.GONE
                    onClick {
                        owner.callToViewRequestFragment()
                    }
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_img_leave_margin)
                    rightOf(R.id.group_info_img_invite)
                }

                imageView(R.drawable.ic_exit_to_app_deep_purple_a200_36dp) {
                    id = R.id.group_info_img_leave_group
                    onClick {
                        owner.leaveGroup()
                    }
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_img_leave_margin)
                    rightOf(R.id.group_info_img_approve)
                }
            }

            swipeRefreshLayout {
                recyclerView {
                    lparams(matchParent, matchParent)
                    layoutManager = LinearLayoutManager(context)
                    backgroundResource = android.R.color.darker_gray
                    padding = dimen(R.dimen.group_screen_recycler_view_padding)
                    adapter = memberListAdapter
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
        verticalPadding = paddingTop
        horizontalPadding = paddingRight
    }
}
