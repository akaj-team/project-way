package vn.asiantech.way.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Fragment create group
 * Created by haingoq on 28/11/2017.
 */
class CreateGroupFragment : BaseFragment() {
    companion object {
        const val KEY_USER = "user"

        /**
         * Get instance with given user.
         */
        fun getInstance(user: User): CreateGroupFragment {
            val instance = CreateGroupFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, user)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var createGroupViewModel: CreateGroupViewModel
    private lateinit var ui: CreateGroupFragmentUI
    private lateinit var user: User
    private var group: Group? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?) = CreateGroupFragmentUI()
            .createView(AnkoContext.create(context, this))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments.getSerializable(KEY_USER) as User
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
                .subscribe(this::createGroupSuccess, this::createGroupFail))
    }

    private fun createGroupSuccess(group: Group) {
        group.ownerId = user.id
        this.group = group
        addDisposables(createGroupViewModel.addUserToGroup(group.ownerId, BodyAddUserToGroup(group.id))
                .observeOnUiThread()
                .subscribe(this::handleAddUserSuccess, this::handleAddUserError))
    }

    private fun createGroupFail(throwable: Throwable) {
        TODO("Will update code later")
    }

    private fun handleAddUserSuccess(user: User) {
        addDisposables(createGroupViewModel.upGroupInfo(group!!)
                .observeOnUiThread()
                .subscribe(this::handleUpGroupInfoSuccess, this::handleUpGroupInfoError))
    }

    private fun handleAddUserError(throwable: Throwable) {
        TODO("Will update code later")
    }

    private fun handleUpGroupInfoSuccess(boolean: Boolean) {
        TODO("Will update code later")
    }

    private fun handleUpGroupInfoError(throwable: Throwable) {
        TODO("Will update code later")
    }
}
