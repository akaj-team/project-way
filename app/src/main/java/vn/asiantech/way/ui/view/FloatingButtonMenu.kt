package vn.asiantech.way.ui.view

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.custom_menu.view.*
import vn.asiantech.way.R


/**
 * Created by haingoq on 29/09/2017.
 */
class FloatingButtonMenu : LinearLayout, View.OnClickListener {
    constructor(context: Context) : super(context) {
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imgMenu -> {
                val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
                val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_alpha)
                imgMenu.startAnimation(anim)
                imgShare.startAnimation(animVisible)
                imgProfile.startAnimation(animVisible)
                imgCalendar.startAnimation(animVisible)
                if (rlShare.visibility == View.VISIBLE) {
                    visibilityAllChildView(View.INVISIBLE)
                } else {
                    visibilityAllChildView(View.VISIBLE)
                }
            }
        }
    }

    private fun initView() {
        View.inflate(context, R.layout.custom_menu, this)
        imgMenu.setOnClickListener(this)
    }

    private fun visibilityAllChildView(visibilityState: Int) {
        rlShare.visibility = visibilityState
        rlProfile.visibility = visibilityState
        rlCalendar.visibility = visibilityState
    }
}
