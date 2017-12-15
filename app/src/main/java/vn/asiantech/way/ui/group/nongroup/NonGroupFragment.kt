package vn.asiantech.way.ui.group.nongroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.ui.base.BaseFragment

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class NonGroupFragment : BaseFragment() {
    private lateinit var ui: NonGroupFragmentUI

    companion object {

        /**
         * Get instance of NonGroupFragment.
         */
        fun getInstance(): NonGroupFragment = NonGroupFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = NonGroupFragmentUI()
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {}

    internal fun eventButtonCreateGroupClick() {
        // Todo handle create group
    }

    internal fun eventViewInviteClick() {
        // Todo handle invite
    }

    internal fun eventButtonBackClick() {
        // Todo handle button back
    }
}
