package vn.asiantech.way.ui.group

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.hypertrack.lib.models.User
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class GroupActivity : BaseActivity() {

    private lateinit var ui: GroupActivityUI
    private val groupViewModel = GroupActivityViewModel()
    private var currentGroupId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = GroupActivityUI()
        ui.setContentView(this)
    }

    override fun onBindViewModel() {
        // TODO: handle later
        addDisposables(
                groupViewModel.getUser().subscribe {
                    this::afterGetUser
                },
                groupViewModel.getGroupId("\"e4e91b20-498b-49a0-b2aa-64b9a992e21d\"")
                        .subscribe {
                            groupViewModel.getUser()
                                    .subscribe {
                                        if (currentGroupId != null && it.groupId != currentGroupId) {
                                            currentGroupId = it.groupId
                                            Log.i("tag11", "reload")
                                        }
                                    }
                        }
        )
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.group_activity_ui_fl_content, fragment)
        transaction.commit()
    }

    private fun afterGetUser(user: User) {
        if (user.groupId != null) {
            currentGroupId = user.groupId
            Log.i("tag11", "show")
        }
    }
}
