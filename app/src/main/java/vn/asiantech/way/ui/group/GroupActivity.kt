package vn.asiantech.way.ui.group

import android.os.Bundle
import com.hypertrack.lib.models.User
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.group.info.GroupInfoFragment
import vn.asiantech.way.ui.group.nongroup.NonGroupFragment

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class GroupActivity : BaseActivity() {

    private lateinit var ui: GroupActivityUI
    private val groupViewModel = GroupActivityViewModel(GroupRepository(), WayRepository())
    private var currentGroupId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = GroupActivityUI()
        ui.setContentView(this)
    }

    override fun onBindViewModel() {
        addDisposables(
                groupViewModel.getUser()
                        .observeOnUiThread()
                        .subscribe(this::handleAfterLoadUserCompleted)
        )
    }

    private fun handleAfterLoadUserCompleted(user: User) {
        addDisposables(groupViewModel.listenerForGroupChange(user.id)
                .observeOnUiThread()
                .subscribe {
                    groupViewModel.getUser()
                            .subscribe(this::handleUserInfo)
                })
        handleUserInfo(user)
    }

    private fun handleUserInfo(user: User) {
        if (user.groupId == null) {
            currentGroupId = ""
            replaceFragment(R.id.group_activity_ui_fr_content, NonGroupFragment.getInstance(user.id))
            return
        }

        if (user.groupId != currentGroupId) {
            currentGroupId = user.groupId
            replaceFragment(R.id.group_activity_ui_fr_content, GroupInfoFragment.getInstance(user.id, currentGroupId))
            return
        }
    }
}
