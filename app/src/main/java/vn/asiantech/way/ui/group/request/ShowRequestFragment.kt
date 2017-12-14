package vn.asiantech.way.ui.group.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.R
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment

/**
 *
 * Created by haingoq on 05/12/2017.
 */
class ShowRequestFragment : BaseFragment() {

    companion object {

        const val KEY_GROUP_ID = "key_group_id"

        /**
         * Get instance of ViewRequestFragment with a given group.
         */
        fun getInstance(groupId: String): ShowRequestFragment {
            val instance = ShowRequestFragment()
            val bundle = Bundle()
            bundle.putString(KEY_GROUP_ID, groupId)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var ui: ShowRequestFragmentUI
    private lateinit var adapter: RequestAdapter
    private lateinit var viewModel: ShowRequestViewModel
    private lateinit var groupId: String
    private var users = mutableListOf<User>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Init groupID
        groupId = arguments.getString(ShowRequestFragment.KEY_GROUP_ID)
        // Init adapter
        adapter = RequestAdapter(users)
        // Init view model
        viewModel = ShowRequestViewModel()
        // Init UI
        ui = ShowRequestFragmentUI(adapter)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel
                        .getRequestsOfUser(groupId)
                        .observeOnUiThread()
                        .subscribe(this::handleGetRequestsOfUserSuccess, this::handleGetRequestsOfUserFailed),

                viewModel
                        .progressDialogObservable
                        .observeOnUiThread()
                        .subscribe(this::updateProgressDialog)
        )
    }

    /**
     * On click button OK of list request
     */
    internal fun eventOnButtonOkClick(userId: String) {
        addDisposables(
                viewModel
                        .addUserToGroup(userId, groupId)
                        .observeOnUiThread()
                        .subscribe(this::handleAddUserToGroupSuccess, this::handleAddUserToGroupFailed)
        )
    }

    /**
     * On click button Cancel of list request
     */
    internal fun eventOnButtonCancelClick(userId: String) {
        handleRemoveRequestInGroup(userId)
    }

    private fun handleGetRequestsOfUserSuccess(user: User) {
        users.clear()
        users.add(user)
        adapter.notifyItemInserted(users.size - 1)
    }

    private fun handleGetRequestsOfUserFailed(error: Throwable) {
        toast(error.message.toString())
    }

    private fun handleAddUserToGroupSuccess(user: User) {
        handleRemoveRequestInGroup(user.id)
    }

    private fun handleAddUserToGroupFailed(error: Throwable) {
        toast(error.message.toString())
    }

    private fun handleRemoveRequestInGroup(userId: String) {
        addDisposables(
                viewModel
                        .removeRequestInGroup(userId, groupId)
                        .observeOnUiThread()
                        .subscribe(this::handleRemoveRequestInGroupSuccess, this::handleRemoveRequestInGroupFailed)
        )
    }

    private fun handleRemoveRequestInGroupSuccess(isSuccess: Boolean) {
        toast(R.string.success)
    }

    private fun handleRemoveRequestInGroupFailed(error: Throwable) {
        toast(error.message.toString())
    }

    private fun updateProgressDialog(show: Boolean) {
        if (show) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }
    }
}
