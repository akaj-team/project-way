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

    companion object {
        internal const val ID_MAP = 2
    }

    internal lateinit var mFabMenuGroups: FloatingButtonHorizontal
    internal lateinit var mFrOverplay: FrameLayout

    override fun createView(ui: AnkoContext<HomeActivity>) = with(ui) {

        relativeLayout {
            lparams(matchParent, matchParent)
            relativeLayout {
                lparams(matchParent, matchParent)

                frameLayout {
                    lparams(matchParent, matchParent)
                    id = ID_MAP
                }

                recyclerView {
                    layoutManager = LinearLayoutManager(context)
                    adapter = homeAdapter
                }.lparams(matchParent, dimen(R.dimen.home_screen_recyclerView_height)) {
                    alignParentBottom()
                    centerVertically()
                    leftMargin = dimen(R.dimen.home_screen_recyclerView_margin)
                    rightMargin = dimen(R.dimen.home_screen_recyclerView_margin)
                    backgroundColor = Color.TRANSPARENT
                }
            }

            mFrOverplay = frameLayout {
                visibility = View.GONE
            }.lparams(matchParent, matchParent) {
                backgroundColor = R.color.colorOverlay
            }

            mFabMenuGroups = floatingButton {}
                    .lparams(wrapContent, wrapContent) {
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
