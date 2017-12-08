package vn.asiantech.way.ui.group.invite

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.EditText
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.extension.onTextChangeListener

/**
 * Invite UI
 * @author NgocTTN
 */

class InviteFragmentUI(val userListAdapter: InviteUserListAdapter) : AnkoComponent<InviteFragment> {

    internal lateinit var edtUserName: EditText

    override fun createView(ui: AnkoContext<InviteFragment>): View = ui.apply {
        verticalLayout {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.colorSearchScreenBackground

            verticalLayout {
                lparams(matchParent, wrapContent)
                gravity = Gravity.CENTER_VERTICAL

                linearLayout {
                    lparams(matchParent, wrapContent)
                    gravity = Gravity.CENTER_VERTICAL

                    // Image Button: back
                    imageButton(R.drawable.ic_back_icon_button) {
                        backgroundResource = Color.TRANSPARENT
                        contentDescription = null
                        padding = dimen(R.dimen.item_user_bottom_or_top_padding)
                        onClick {
                            owner.onBackPressed()
                        }
                    }.lparams(wrapContent, wrapContent) {
                        topMargin = dimen(R.dimen.invite_screen_padding)
                        horizontalMargin = dimen(R.dimen.invite_screen_padding)
                    }

                    // Edit text : enter user name
                    edtUserName = editText {
                        backgroundResource = R.color.colorEdtSearchBackground
                        hint = resources.getString(R.string.item_user_text_hint)
                        singleLine = true
                        padding = dimen(R.dimen.invite_screen_padding)
                        textSize = px2dip(dimen(R.dimen.invite_screen_edt_user_name_text_size))
                        gravity = Gravity.CENTER_VERTICAL
                        onTextChangeListener({}, {
                            owner.onGetListUserInvite(it.toString())
                        }, {})
                    }.lparams(matchParent, matchParent) {
                        topMargin = dimen(R.dimen.invite_screen_padding)
                        rightMargin = dimen(R.dimen.invite_screen_padding)
                    }
                }

                // RecycleView: list user invite
                recyclerView {
                    id = R.id.invite_recycle_view
                    clipToPadding = false
                    backgroundResource = R.color.colorWhite
                    userListAdapter.onItemInviteClick = {
                        owner.onItemInviteClick(it)
                    }
                    layoutManager = LinearLayoutManager(context)
                    adapter = userListAdapter
                }.lparams(matchParent, matchParent) {
                    topMargin = dimen(R.dimen.invite_screen_top_margin)
                    bottomMargin = dimen(R.dimen.invite_screen_padding)
                    horizontalMargin = dimen(R.dimen.invite_screen_padding)
                }
            }
        }
    }.view
}
