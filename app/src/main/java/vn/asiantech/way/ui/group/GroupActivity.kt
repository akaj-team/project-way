package vn.asiantech.way.ui.group

import android.os.Bundle
import android.support.v4.app.Fragment
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.group.request.ShowRequestFragment

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
        replaceFragment(ShowRequestFragment.getInstance("e97b3ba1-0de7-4cef-8322-0f868913f709addclose"))
    }

    override fun onBindViewModel() {
        // TODO: handle later
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.group_activity_ui_fr_content, fragment)
        transaction.commit()
    }
}
