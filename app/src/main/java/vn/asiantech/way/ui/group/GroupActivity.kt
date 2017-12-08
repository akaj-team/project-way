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
        supportFragmentManager.beginTransaction().replace(R.id.group_activity_ui_fr_content, ViewInviteFragment.getInstance("543984f6-2642-4c24-97f8-79c92adf1630")).commit()
    }

    override fun onBindViewModel() {
        // TODO: handle later
    }
}
