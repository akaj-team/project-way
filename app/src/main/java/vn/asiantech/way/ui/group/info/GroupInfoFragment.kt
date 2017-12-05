package vn.asiantech.way.ui.group.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.utils.AppConstants

/**
 * Fragment group user
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragment : BaseFragment() {

    private lateinit var ui: GroupInfoFragmentUI
    private val groupInfoViewModel = GroupInfoViewModel()
    private var userId = ""
    private var groupId = ""
    private val members = mutableListOf<User>()

    companion object {
        fun getInstance(userId: String, groupId: String): GroupInfoFragment {
            val instance = GroupInfoFragment()
            val bundle = Bundle()
            bundle.putString(AppConstants.KEY_USER_ID, userId)
            bundle.putString(AppConstants.KEY_GROUP_ID, groupId)
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments.getString(AppConstants.KEY_USER_ID)
        groupId = arguments.getString(AppConstants.KEY_GROUP_ID)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = GroupInfoFragmentUI(userId, members)
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        addDisposables(
                groupInfoViewModel.getGroupInfo(groupId).subscribe {
                    Log.i("tag11", Gson().toJson(it).toString())
                }
        )
    }
}
