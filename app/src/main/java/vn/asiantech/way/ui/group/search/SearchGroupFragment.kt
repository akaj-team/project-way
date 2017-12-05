package vn.asiantech.way.ui.group.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.data.model.Group
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

    private lateinit var adapter: SearchGroupAdapter
    private var groups = mutableListOf<Group>()
    private lateinit var ui: SearchGroupFragmentUI

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = SearchGroupFragmentUI()
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchGroupAdapter(context, groups) {

        }
    }

    override fun onBindViewModel() {

    }
}
