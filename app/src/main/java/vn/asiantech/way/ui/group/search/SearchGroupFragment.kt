package vn.asiantech.way.ui.group.search

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        fun getInstance(userId: String): SearchGroupFragment {
            val instance = SearchGroupFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, userId)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var ui: SearchGroupFragmentUI
    private lateinit var progressDialog: ProgressDialog
    private lateinit var viewModel: SearchGroupViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val userId = arguments.getString(KEY_USER)
        viewModel = SearchGroupViewModel(userId)
        ui = SearchGroupFragmentUI(viewModel.groups, context)
        ui.searchGroupAdapter.onJoinButtonClick = {
            eventOnJoinButtonClicked(it)
        }
        initProgressDialog()
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                viewModel.progressDialogObservable
                        .observeOnUiThread()
                        .subscribe(this::updateProgressDialog),
                viewModel.updateAutocompleteList
                        .observeOnUiThread()
                        .subscribe(
                                this::handleRecyclerViewGroupWhenSearchSuccess,
                                this::handleSearchGroupError
                        ),
                viewModel.updateCurrentRequest
                        .observeOnUiThread()
                        .subscribe(
                                this::handleWhenUpdateCurrentRequestSuccess,
                                this::handleWhenUpdateCurrentRequestError
                        )
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

    private fun handleRecyclerViewGroupWhenSearchSuccess(diff: DiffUtil.DiffResult) {
        diff.dispatchUpdatesTo(ui.searchGroupAdapter)
    }

    private fun handleSearchGroupError(error: Throwable) {
        toast(error.message.toString())
    }

    private fun eventOnJoinButtonClicked(group: Group) {
        addDisposables(
                viewModel
                        .postRequestToGroup(group)
                        .observeOnUiThread()
                        .subscribe(
                                this::handlePostRequestToGroupSuccess,
                                this::handlePostRequestToGroupError
                        )
        )
    }

    private fun handlePostRequestToGroupSuccess(isSuccess: Boolean) {
        toast(getString(R.string.success))
    }

    private fun handlePostRequestToGroupError(error: Throwable) {
        toast(error.message.toString())
    }

    private fun updateProgressDialog(show: Boolean) {
        if (show) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    private fun handleWhenUpdateCurrentRequestSuccess(invite: Invite) {
        ui.searchGroupAdapter.updateCurrentRequest(invite)
        ui.searchGroupAdapter.notifyDataSetChanged()
    }

    private fun handleWhenUpdateCurrentRequestError(error: Throwable) {
        toast(error.message.toString())
    }
}
