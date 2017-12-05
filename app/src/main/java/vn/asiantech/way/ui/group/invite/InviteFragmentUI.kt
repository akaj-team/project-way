package vn.asiantech.way.ui.group.invite

import android.app.Activity
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.hypertrack.lib.models.User
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.ui.search.SearchActivityUI

/**
 * Invite UI
 * @author NgocTTN
 */

class InviteFragmentUI(val activity: Activity, private val users: MutableList<User>) : AnkoComponent<InviteFragment> {

    private var userId = ""
    private var groupId = ""
    private var groupName = ""
    private var ownerId = ""
    internal lateinit var edtUserName: EditText
    internal val userListAdapter = UserListAdapter(users)

    override fun createView(ui: AnkoContext<InviteFragment>): View = ui.apply {
        verticalLayout {
            lparams(matchParent, wrapContent)
            padding = dip(value = 10)
            backgroundColor = ActivityCompat.getColor(context, R.color.colorSearchScreenBackground)

            verticalLayout {
                lparams(matchParent, wrapContent)
                gravity = Gravity.CENTER_VERTICAL

                imageButton(R.drawable.ic_back_icon_button) {
                    backgroundColor = Color.TRANSPARENT
                    contentDescription = null
                    onClick {
                        activity.onBackPressed()
                    }
                }.lparams(wrapContent, wrapContent)

                edtUserName = editText {
                    backgroundColor = Color.BLACK
                    hint = resources.getString(R.string.enter_user_name)
                    singleLine = true
                    padding = dip(value = 10)
                    textSize = resources.getDimension(R.dimen.search_screen_text_size)

                    addTextChangedListener(object : SearchActivityUI.TextChangeListener {
                        override fun afterTextChanged(editable: Editable) {
                            owner.searchUserList(editable.toString().trim())
                        }
                    })
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(value = 10)
                    rightMargin = dip(value = 10)
                }

                // RecycleView
                recyclerView {
                    id = R.id.invite_recycle_view
                    userListAdapter.onItemClick = {
                        owner.onItemClick(it)
                    }
                    layoutManager = LinearLayoutManager(context)
                    adapter = userListAdapter
                }.lparams(matchParent, matchParent) {
                    topMargin = dip(value = 20)
                }
            }
        }
    }.view
}
