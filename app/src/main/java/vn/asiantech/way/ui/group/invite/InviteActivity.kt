package vn.asiantech.way.ui.group.invite

import android.os.Bundle
import org.jetbrains.anko.setContentView
import vn.asiantech.way.ui.base.BaseActivity

/**
 * Invite Activity
 */
class InviteActivity : BaseActivity() {
    private lateinit var ui: InviteUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = InviteUI()
        ui.setContentView(this)
    }

    override fun onBindViewModel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
