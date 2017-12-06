package vn.asiantech.way.ui.group

import android.os.Bundle
import android.util.Log
import com.hypertrack.lib.models.User
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.group.info.GroupInfoFragment

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class GroupActivity : BaseActivity() {

    private lateinit var ui: GroupActivityUI
    private val groupViewModel = GroupActivityViewModel()
    private var user: User? = null
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

    override fun onPause() {
        super.onPause()
        user = null
    }

    private fun handleAfterLoadUserCompleted(newUser: User) {
        Log.i("tag11", "aaaaaaaaaaa")
        if (user == null) {
            Log.i("tag11", "111111111111")
            addDisposables(groupViewModel.listenerForGroupChange(newUser.id)
                    .observeOnUiThread()
                    .subscribe {
                        groupViewModel.getUser()
                                .subscribe(this::handleAfterReloadUserCompleted)
                    })
        }
        user = newUser
        if (newUser.groupId != null && currentGroupId != newUser.groupId) {
            currentGroupId = newUser.groupId
            Log.i("tag11", "replace")
            replaceFragment(R.id.group_activity_ui_fr_content,
                    GroupInfoFragment.getInstance(newUser.id, newUser.groupId))
        }
    }

    private fun handleAfterReloadUserCompleted(newUser: User) {
        if (newUser.groupId != null && newUser.groupId != currentGroupId) {
            currentGroupId = newUser.groupId
            Log.i("tag11", "load lai")
            replaceFragment(R.id.group_activity_ui_fr_content,
                    GroupInfoFragment.getInstance(newUser.id, newUser.groupId))
        } else {
            Log.i("tag11", "xxxxxxxxxxxx")
        }
    }
}
