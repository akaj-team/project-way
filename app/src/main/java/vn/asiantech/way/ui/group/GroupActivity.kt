package vn.asiantech.way.ui.group

import android.os.Bundle
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.group.showinvite.ViewInviteFragment

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
        supportFragmentManager.beginTransaction().replace(R.id.group_activity_ui_fr_content, ViewInviteFragment.getInstance("0f3831a1-8131-4e70-be02-f6f85b1936f6")).commit()
    }

    override fun onBindViewModel() {
        // TODO: handle later
    }
}
