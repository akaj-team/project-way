package vn.asiantech.way.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Fragment create group
 * Created by haingoq on 28/11/2017.
 */
class CreateGroupFragment : BaseFragment() {
    lateinit var createGroupViewModel: CreateGroupViewModel
    lateinit var ui: CreateGroupFragmentUI
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?) = CreateGroupFragmentUI()
            .createView(AnkoContext.create(context, this))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui = CreateGroupFragmentUI()
        createGroupViewModel = CreateGroupViewModel(activity)
    }

    override fun onBindViewModel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Create Group
     */
    internal fun createGroup() {
        addDisposables(createGroupViewModel.createGroup(ui.edtGroupName.text.toString())
                .observeOnUiThread()
                .subscribe({
                    //TODO: update logic more later
                    toast("Create Group Success")
                }, {
                    toast("$it")
                }))
    }
}
