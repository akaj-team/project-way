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
                GroupRepository().acceptInvite("0f3831a1-8131-4e70-be02-f6f85b1936f6",
                        Invite("0f3831a1-8131-4e70-be02-f6f85b1936f6",
                                "b5094b64-a182-4077-a8b5-66284a34f98b",
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
