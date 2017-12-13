package vn.asiantech.way.ui.home

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R
import vn.asiantech.way.ui.custom.FloatingMenuButton
import vn.asiantech.way.ui.custom.floatingButton

/**
 * Created at 11 / 2017
 * Created by at-hoavo on 27/11/2017.
 */
class HomeActivityUI(private val homeAdapter: HomeAdapter) : AnkoComponent<HomeActivity> {

    internal lateinit var fabMenuGroup: FloatingMenuButton
    internal lateinit var recycleViewLocation: RecyclerView

    override fun createView(ui: AnkoContext<HomeActivity>) = with(ui) {

        relativeLayout {
            lparams(matchParent, matchParent)
            relativeLayout {
                lparams(matchParent, matchParent)
                frameLayout {
                    lparams(matchParent, matchParent)
                    id = R.id.home_activity_fr_map
                }
                recycleViewLocation = recyclerView {
                    backgroundColor = Color.TRANSPARENT
                    layoutManager = LinearLayoutManager(context)
                    adapter = homeAdapter
                }.lparams(matchParent, dimen(R.dimen.home_screen_recyclerView_height)) {
                    alignParentBottom()
                    centerVertically()
                    leftMargin = dimen(R.dimen.home_screen_recyclerView_margin)
                    rightMargin = dimen(R.dimen.home_screen_recyclerView_margin)
                }
            }
            fabMenuGroup = floatingButton(object : FloatingMenuButton.OnMenuClickListener {
                override fun eventItemMenuClicked(view: View) {
                    owner.eventOnClickItemMenu(view)
                }
            }) {
            }.lparams {
                alignParentBottom()
                alignParentRight()
            }
        }
    }
}
