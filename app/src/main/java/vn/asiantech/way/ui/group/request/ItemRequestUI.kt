package vn.asiantech.way.ui.group.request

import android.view.ViewGroup
import org.jetbrains.anko.*

/**
 * Created by haingoq on 05/12/2017.
 */
class ItemRequestUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            lparams(matchParent, wrapContent)
        }
    }
}