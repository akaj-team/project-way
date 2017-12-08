package vn.asiantech.way.ui.group.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.R
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Fragment create group
 * Created by haingoq on 28/11/2017.
 */
class CreateGroupFragment : BaseFragment() {
    companion object {
        private const val KEY_USER_ID = "user_id"

        /**
         * Get instance with given user.
         */
        fun getInstance(userId: String): CreateGroupFragment {
            val instance = CreateGroupFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER_ID, userId)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var createGroupViewModel: CreateGroupViewModel
    private lateinit var ui: CreateGroupFragmentUI
    private var userId: String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = CreateGroupFragmentUI()
        return ui.createView(AnkoContext.create(context, this))
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments.getString(KEY_USER_ID)
        createGroupViewModel = CreateGroupViewModel()
    }

    override fun onBindViewModel() {
        // Nothing to do
    }

    internal fun eventOnViewClicked(view: View) {
        when (view.id) {
            R.id.create_group_fragment_ui_btn_back -> activity.onBackPressed()

            R.id.create_group_fragment_ui_btn_create ->
                createGroup(ui.edtGroupName.text.toString().trim())
        }
    }

    private fun createGroup(groupName: String) {
        if (groupName.isNotEmpty()) {
            addDisposables(createGroupViewModel.createGroup(groupName, userId)
                    .observeOnUiThread()
                    .subscribe(this::handleCreateGroupSuccess, this::handleCreateGroupError))
        } else {
            toast(R.string.group_name_empty)
        }
    }

    private fun handleCreateGroupError(throwable: Throwable) {
        toast(R.string.error_message)
    }

    private fun handleCreateGroupSuccess(boolean: Boolean) {
        TODO("Will update code later")
    }
}
