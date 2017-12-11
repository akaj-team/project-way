package vn.asiantech.way.ui.group.search

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Group
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

    private lateinit var adapter: GroupListAdapter
    private lateinit var ui: SearchGroupFragmentUI
    private lateinit var progressDialog: ProgressDialog

    private lateinit var viewModel: SearchGroupViewModel
    private var groups = mutableListOf<Group>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val userId = arguments.getString(KEY_USER)
        viewModel = SearchGroupViewModel(userId)
        adapter = GroupListAdapter(context, groups, viewModel.currentRequest) {
            eventOnJoinButtonClicked(it)
        }
        ui = SearchGroupFragmentUI(adapter)
        initProgressDialog()
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(viewModel
                .triggerSearchGroup()
                .subscribe(
                        this::handleRecyclerViewGroupWhenSearchSuccess,
                        this::handleSearchGroupError
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

    private fun handleRecyclerViewGroupWhenSearchSuccess(data: List<Group>) {
        adapter.updateCurrentRequest(viewModel.currentRequest)
        groups.clear()
        groups.addAll(data)
        adapter.notifyDataSetChanged()
    }

    private fun handleSearchGroupError(error: Throwable) {
        toast(error.message.toString())
    }

    private fun eventOnJoinButtonClicked(group: Group) {
        addDisposables(
                viewModel
                        .postRequestToGroup(group)
                        .subscribe(
                                this::handlePostRequestToGroupSuccess,
                                this::handlePostRequestToGroupError
                        )
        )
    }

    private fun handlePostRequestToGroupSuccess(isSuccess: Boolean) {
        toast(getString(R.string.success))
        adapter.updateCurrentRequest(viewModel.currentRequest)
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
