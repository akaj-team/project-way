package vn.asiantech.way.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Fragment group user
 * Created by haingoq on 28/11/2017.
 */
class GroupInfoFragment: BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return GroupInfoFragmentUI().createView(AnkoContext.Companion.create(context, this))
    }
}
