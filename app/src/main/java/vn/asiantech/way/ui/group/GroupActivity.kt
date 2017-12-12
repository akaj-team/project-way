package vn.asiantech.way.ui.group

import android.os.Bundle
import android.util.Log
import com.hypertrack.lib.models.User
import org.jetbrains.anko.setContentView
import vn.asiantech.way.ui.base.BaseActivity

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class GroupActivity : BaseActivity() {

    private lateinit var ui: GroupActivityUI
    private val groupViewModel = GroupActivityViewModel()
    private var currentGroupId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = GroupActivityUI()
        ui.setContentView(this)
    }

    override fun onBindViewModel() {
        addDisposables(
                groupViewModel.getUser().subscribe(this::handleAfterLoadUserCompleted)
        )
    }

    private fun handleAfterLoadUserCompleted(user: User) {
        Log.i("tag11", "111111111111")
        addDisposables(groupViewModel.listenerForGroupChange(user.id)
                .subscribe {
                    groupViewModel.getUser()
                            .subscribe(this::handleUserInfo)
                })
        handleUserInfo(user)
    }

    private fun handleUserInfo(user: User) {
        if (user.groupId == null) {
            // TODO: Replace HomeFragment
            currentGroupId = ""
            Log.i("tag11", "121212121212")
            return
        }

        if (user.groupId != currentGroupId) {
            currentGroupId = user.groupId
            // TODO: Replace GroupInfoFragment
            Log.i("tag11", "load lai")
            return
        }
        Log.i("tag11", "xxxxxxxxxxxx")
    }
}
