package vn.asiantech.way.ui.group

import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.setContentView
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseActivity
import vn.asiantech.way.ui.group.search.SearchGroupFragment

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
        addDisposables(groupViewModel.getUser()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    supportFragmentManager.beginTransaction().replace(R.id.group_activity_fr, SearchGroupFragment.getInstance(it)).commit()
                })
    }
}
