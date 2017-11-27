package vn.asiantech.way.ui.register

import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.R

/**
 * Created by haingoq on 27/11/2017.
 */
class RegisterActivityUI : AnkoComponent<RegisterActivity> {
    override fun createView(ui: AnkoContext<RegisterActivity>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            frameLayout {
                circleImageView {
                    lparams(R.dimen.register_screen_avatar_size, R.dimen.register_screen_avatar_size)
                    backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.ic_default_avatar)
                    borderColor = ContextCompat.getColor(context, R.color.white)
                    borderWidth = dip(1)
                }
                progressBar {
                    visibility = View.GONE
                }.lparams {
                    gravity = Gravity.CENTER
                }
                circleImageView {

                }.lparams {
                    gravity = Gravity.END
                    backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.ic_profile_camera)
                    rightMargin = R.dimen.register_screen_avatar_margin
                }
            }.lparams {
                topMargin = dip(56)
                centerHorizontally()
            }
        }
    }

    inline fun ViewManager.circleImageView(theme: Int = 0, init: CircleImageView.() -> Unit): CircleImageView {
        return ankoView({ CircleImageView(it) }, theme, init)
    }
}