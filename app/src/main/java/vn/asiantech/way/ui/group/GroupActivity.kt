package vn.asiantech.way.ui.group

import android.os.Bundle
import android.util.Log
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
    }

    override fun onBindViewModel() {
        // TODO: handle later
        addDisposables(groupViewModel
                .getGroupId("e4e91b20-498b-49a0-b2aa-64b9a992e21d")
                .subscribe { Log.i("tag11", it + "----") })
    }
}
