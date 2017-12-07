package vn.asiantech.way.ui.group.info

import android.graphics.Color
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
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
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragmentUI(userId: String, ownerId: String, members: MutableList<User>)
    : AnkoComponent<GroupInfoFragment> {

    internal lateinit var tvGroupName: TextView
    internal lateinit var tvMembersCount: TextView
    internal lateinit var tvCreateAt: TextView
    internal lateinit var imgApprove: ImageView
    internal lateinit var swipeRefreshLayout: SwipeRefreshLayout
    internal val memberListAdapter = MemberListAdapter(userId, ownerId, members)

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
                horizontalPadding = padding
            }.lparams(matchParent, dimen(R.dimen.toolbar_height))

            tvMembersCount = textViewInfo()
            tvCreateAt = textViewInfo()
            textViewInfo(R.string.members_list)

            relativeLayout {
                lparams(matchParent, wrapContent)
                padding = dimen(R.dimen.group_screen_tv_count_padding_left)

                imageView(R.drawable.ic_person_add_deep_purple_a200_36dp) {
                    id = R.id.group_info_img_invite
                    onClick { it ->
                        owner.eventViewOnClicked(it!!)
                    }
                }

                imgApprove = imageView(R.drawable.ic_spellcheck_deep_purple_a200_36dp) {
                    id = R.id.group_info_img_approve
                    visibility = View.GONE
                    onClick { it ->
                        owner.eventViewOnClicked(it!!)
                    }
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_img_leave_margin)
                    rightOf(R.id.group_info_img_invite)
                }

                imageView(R.drawable.ic_exit_to_app_deep_purple_a200_36dp) {
                    id = R.id.group_info_img_leave_group
                    onClick { it ->
                        owner.eventViewOnClicked(it!!)
                    }
                }.lparams {
                    leftMargin = dimen(R.dimen.group_screen_img_leave_margin)
                    rightOf(R.id.group_info_img_approve)
                }
            }

            swipeRefreshLayout = swipeRefreshLayout {
                onRefresh {
                    owner.handleSwipeRefreshLayoutOnRefresh()
                }
                recyclerView {
                    lparams(matchParent, matchParent)
                    layoutManager = LinearLayoutManager(context)
                    backgroundResource = android.R.color.darker_gray
                    topPadding = dimen(R.dimen.group_screen_recycler_view_padding)
                    memberListAdapter.onImageUpToAdminClick = {
                        owner.eventImageUpToAdminClicked(it)
                    }
                    adapter = memberListAdapter
                }
            }.lparams(matchParent, matchParent)
        }
    }

    private fun ViewManager.textViewInfo(@StringRes resourceId: Int = R.string.empty_string) = textView(resourceId) {
        textSize = px2dip(dimen(R.dimen.group_text_size_normal))
    }.apply {
        verticalPadding = dimen(R.dimen.group_screen_recycler_view_padding)
        horizontalPadding = dimen(R.dimen.group_screen_tv_count_padding_left)
    }
}
