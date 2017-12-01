package vn.asiantech.way.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Fragment create group
 * Created by haingoq on 28/11/2017.
 */
class CreateGroupFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?) = CreateGroupFragmentUI()
            .createView(AnkoContext.create(context, this))

    override fun onBindViewModel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
