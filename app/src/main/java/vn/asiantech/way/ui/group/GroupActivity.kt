package vn.asiantech.way.ui.group

import android.os.Bundle
import android.util.Log
import org.jetbrains.anko.setContentView
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
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

        addDisposables(
                GroupRepository().acceptInvite("543984f6-2642-4c24-97f8-79c92adf1630",
                        Invite("543984f6-2642-4c24-97f8-79c92adf1630",
                                "9b8ba7a8-b58f-41cd-a53b-4a60ac62b2c7",
                                "XXX",
                                true))
                        .subscribe({
                            Log.i("tag11", "ok")
                        }, {
                            Log.i("tag11", "non-ok")
                        })
        )
    }

    override fun onBindViewModel() {
        // TODO: handle later
    }
}
