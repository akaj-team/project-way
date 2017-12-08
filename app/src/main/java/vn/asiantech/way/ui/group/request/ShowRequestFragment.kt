package vn.asiantech.way.ui.group.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import org.jetbrains.anko.AnkoContext
import vn.asiantech.way.ui.base.BaseFragment
import java.util.*

/**
 *
 * Created by haingoq on 05/12/2017.
 */
class ShowRequestFragment : BaseFragment() {
    private lateinit var ui: ShowRequestFragmentUI
    private var users: MutableList<User> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ui = ShowRequestFragmentUI(RequestAdapter(users))
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onBindViewModel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
