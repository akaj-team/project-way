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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = GroupActivityUI()
        ui.setContentView(this)
    }

    override fun onBindViewModel() {
        addDisposables(
                groupViewModel.getUser()
                        .observeOnUiThread()
                        .subscribe(this::afterLoadUser)
                )
    }

    private fun afterLoadUser(user: User) {
        addDisposables(groupViewModel.listenerForGroupChange(user.id)
                .observeOnUiThread()
                .subscribe {
                    Log.i("tag11", "ssssss")
                })
        if (user.groupId != null) {
            replaceFragment(R.id.group_activity_ui_fr_content,
                    GroupInfoFragment.getInstance(user.id, user.groupId))
        }
    }
}
