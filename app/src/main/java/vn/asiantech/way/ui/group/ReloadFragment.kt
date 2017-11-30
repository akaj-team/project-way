package vn.asiantech.way.ui.group

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
class ReloadFragment : BaseFragment() {
    private lateinit var ui: ReloadFragmentUI
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = ReloadFragmentUI()
        return ui.createView(AnkoContext.create(context, this))
    }
}
