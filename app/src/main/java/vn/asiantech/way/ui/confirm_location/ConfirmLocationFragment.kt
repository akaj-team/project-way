package vn.asiantech.way.ui.confirm_location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vn.asiantech.way.R
import vn.asiantech.way.ui.base.BaseFragment

/**
 * Fragment confirm location
 * Created by haingoq on 10/10/2017.
 */
class ConfirmLocationFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.fragment_confirm_location, container, false)
        return view
    }
}
