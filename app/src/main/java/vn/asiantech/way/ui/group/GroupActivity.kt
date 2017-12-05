package vn.asiantech.way.ui.group

import android.os.Bundle
import org.jetbrains.anko.setContentView
import vn.asiantech.way.ui.base.BaseActivity

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
        //replaceFragment(R.id.group_activity_ui_fr_content, GroupInfoFragment.getInstance("hhhhhhhasdasdasd", "b8f8a472-8331-4c2f-8b28-bdbdb726d2c5"))
    }

    override fun onBindViewModel() {
        // TODO: handle later
    }
}
