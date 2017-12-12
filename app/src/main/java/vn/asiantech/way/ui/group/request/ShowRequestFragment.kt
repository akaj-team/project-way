package vn.asiantech.way.ui.group.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment
import java.util.*

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

    private var requestsUser: MutableList<User> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Init groupID
        groupId = arguments.getString(ShowRequestFragment.KEY_GROUP_ID)
        // Init adapter
        adapter = RequestAdapter(requestsUser)
        // Init view model
        viewModel = ShowRequestViewModel()
        // Init UI
        ui = ShowRequestFragmentUI(RequestAdapter(requestsUser))
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel
                        .getRequestsOfUser(groupId)
                        .subscribe(this::handleGetRequestsOfUserSuccess),

                viewModel
                        .progressDialogObservable
                        .subscribe(this::updateProgressDialog)
        )
    }

    internal fun eventOnButtonOkClick(userId: String) {
        addDisposables(
                viewModel
                        .addUserToGroup(userId, groupId)
                        .subscribe(this::handleAddUserToGroupSuccess)
        )
    }

    internal fun eventOnButtonCancelClick(userId: String) {
        handleCancelAddUserToGroup(userId)
    }

    private fun handleGetRequestsOfUserSuccess(user: User) {
        requestsUser.add(user)
        adapter.notifyDataSetChanged()
    }

    private fun updateProgressDialog(show: Boolean) {
        if (show) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }
    }

    private fun handleCancelAddUserToGroup(userId: String) {
        val requestRef = firebaseDatabase.getReference("group/$groupId" +
                "/request/$userId")
        requestRef.removeValue()
        val userRequestRef = firebaseDatabase.getReference("user/$userId" +
                "/request")
        userRequestRef.removeValue()
    }

    private fun handleAddUserToGroupSuccess(user: User) {
        val userId = user.id
        val requestRef = firebaseDatabase.getReference("group/$groupId" +
                "/request/$userId")
        requestRef.removeValue()
        val userRequestRef = firebaseDatabase.getReference("user/$userId" +
                "/request")
        userRequestRef.removeValue()
        val userGroup = firebaseDatabase.getReference("user/$userId/groupId")
        userGroup.setValue(groupId)
        toast(R.string.success)
    }
}
