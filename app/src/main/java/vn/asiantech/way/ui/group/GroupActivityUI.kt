package vn.asiantech.way.ui.group

import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import vn.asiantech.way.R

/**
 *
 * Created by haingoq on 29/11/2017.
 */
class GroupActivityUI : AnkoComponent<GroupActivity> {
    override fun createView(ui: AnkoContext<GroupActivity>) = with(ui) {
        frameLayout {
            id = R.id.group_activity_fr
            lparams(matchParent, matchParent)
            id = R.id.group_activity_ui_fr_content
        }
    }
}
