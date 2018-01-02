package vn.asiantech.way.ui.group.nongroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.R
import vn.asiantech.way.extension.replaceFragment
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.ui.group.create.CreateGroupFragment

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class NonGroupFragment : BaseFragment() {
    companion object {
        private const val KEY_USER_ID = "user_id"

        /*
         * Get instance with given userId
         */
        fun getInstance(userId: String): NonGroupFragment {
            val instance = NonGroupFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER_ID, userId)
            instance.arguments = bundle
            return instance
        }
    }

    private lateinit var ui: NonGroupFragmentUI
    private lateinit var userId: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = NonGroupFragmentUI()
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments.getString(KEY_USER_ID)
    }

    override fun onBindViewModel() {
        // no-op
    }

    internal fun onClickView(view: View?) {
        when (view) {
            ui.btnCreateGroup -> activity.replaceFragment(R.id.group_activity_ui_fr_content, CreateGroupFragment.getInstance(userId))
            ui.btnViewInvite -> toast(R.string.coming_soon)
            ui.btnBack -> activity.onBackPressed()
        }
    }
}
