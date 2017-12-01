package vn.asiantech.way.ui.home

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewManager
import android.widget.FrameLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import vn.asiantech.way.R
import vn.asiantech.way.ui.custom.FloatingButtonHorizontal

/**
 * Created at 11 / 2017
 * Created by at-hoavo on 27/11/2017.
 */
class HomeActivityUI(private val homeAdapter: HomeAdapter) : AnkoComponent<HomeActivity> {

    internal lateinit var fabMenuGroup: FloatingButtonHorizontal
    internal lateinit var frOverlay: FrameLayout

    override fun createView(ui: AnkoContext<HomeActivity>) = with(ui) {

        relativeLayout {
            lparams(matchParent, matchParent)
            relativeLayout {
                lparams(matchParent, matchParent)

                frameLayout {
                    lparams(matchParent, matchParent)
                    id = R.id.home_activity_fr_map
                }

                recyclerView {
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

            frOverlay = frameLayout {
                visibility = View.GONE
                backgroundResource = R.color.colorOverlay
            }.lparams(matchParent, matchParent)

            fabMenuGroup = floatingButton {}
                    .lparams {
                        alignParentBottom()
                        alignParentRight()
                        margin = dimen(R.dimen.home_screen_floating_button_margin)
                    }
        }
    }
}

/**
 * Function to custom floatingButton
 */
inline fun ViewManager.floatingButton(init: FloatingButtonHorizontal.() -> Unit)
        : FloatingButtonHorizontal {
    return ankoView({ FloatingButtonHorizontal(it) }, theme = 0, init = init)
}
