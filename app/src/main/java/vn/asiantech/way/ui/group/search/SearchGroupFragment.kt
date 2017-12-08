package vn.asiantech.way.ui.group.search

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupFragment : BaseFragment() {

    companion object {

        const val KEY_USER = "key_user"

        /**
         * Get instance of SearchGroupFragemt with a given user.
         */
        fun getInstance(user: User): SearchGroupFragment {
            val instance = SearchGroupFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, user)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var user: User
    private lateinit var adapter: GroupListAdapter
    private lateinit var ui: SearchGroupFragmentUI
    private lateinit var progressDialog: ProgressDialog

    private val viewModel = SearchGroupViewModel()
    private var groups = mutableListOf<Group>()
    private var currentRequest: Invite = Invite("", "", "", false)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        adapter = GroupListAdapter(context, groups, currentRequest) {
            eventOnJoinButtonClicked(it)
        }
        ui = SearchGroupFragmentUI(adapter)
        initProgressDialog()
        user = arguments.getSerializable(KEY_USER) as User
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(viewModel
                .getCurrentRequest(user.id)
                .subscribe(
                        this::handleGetCurrentRequestSuccess,
                        this::handleGetCurrentRequestError
                ),
                viewModel.progressDialogObservable
                        .observeOnUiThread()
                        .subscribe(this::updateProgressDialog)
        )
    }

    internal fun eventOnTextChangedSearchGroup(query: String) {
        viewModel.eventAfterTextChanged(query)
    }

    internal fun eventOnBackClick() {
        //TODO : Send broadcast to GroupActivity
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setMessage(getString(R.string.processing))
    }

    private fun handleGetCurrentRequestSuccess(invite: Invite) {
        currentRequest = invite
        addDisposables(
                viewModel
                        .searchGroupQuery()
                        .subscribe(
                                this::handleRecyclerViewGroupWhenSearchSuccess,
                                this::handleSearchGroupError
                        )
        )
    }

    private fun handleGetCurrentRequestError(error: Throwable) {
        toast(error.message.toString())
    }

    private fun handleRecyclerViewGroupWhenSearchSuccess(data: List<Group>) {
        adapter.updateCurrentRequest(currentRequest)
        groups.clear()
        groups.addAll(data)
        adapter.notifyDataSetChanged()
    }

    private fun handleSearchGroupError(error: Throwable) {
        toast(error.message.toString())
    }

    private fun eventOnJoinButtonClicked(group: Group) {
        val invite = Invite(user.id, group.id, group.name, true)
        addDisposables(
                viewModel
                        .postRequestToGroup(group.id, invite)
                        .doOnSuccess {
                            currentRequest = invite
                        }
                        .subscribe(
                                this::handlePostRequestToGroupSuccess,
                                this::handlePostRequestToGroupError
                        )
        )
    }

    private fun handlePostRequestToGroupSuccess(isSuccess: Boolean) {
        toast(getString(R.string.success))
        adapter.updateCurrentRequest(currentRequest)
        adapter.notifyDataSetChanged()
    }

    private fun handlePostRequestToGroupError(error: Throwable) {
        toast(error.message.toString())
    }

    private fun updateProgressDialog(isShow: Boolean) {
        if (isShow) {
            progressDialog.dismiss()
        } else {
            progressDialog.show()
        }
    }
}
