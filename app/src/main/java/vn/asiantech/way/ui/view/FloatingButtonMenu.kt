package vn.asiantech.way.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import vn.asiantech.way.R


/**
 * Created by haingoq on 29/09/2017.
 */
class FloatingButtonMenu : LinearLayout, View.OnClickListener {
    val mFabMenu: CustomFloatingButton = CustomFloatingButton(context)
    val mFabShare: CustomFloatingButton = CustomFloatingButton(context)
    val mFabProfile: CustomFloatingButton = CustomFloatingButton(context)
    val mFabCalendar: CustomFloatingButton = CustomFloatingButton(context)
    var isVisible: Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context) : super(context) {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        initFloatingButton(mFabCalendar, R.drawable.ic_profile, "Calendar")
        initFloatingButton(mFabProfile, R.drawable.ic_profile, "Profile")
        initFloatingButton(mFabShare, R.drawable.ic_share, "Share")
        initFloatingMenu()
    }

    override fun onClick(p0: View?) {
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
        mFabMenu.startAnimation(anim)
        val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_alpha)
        mFabShare.startAnimation(animVisible)
        mFabProfile.startAnimation(animVisible)
        mFabCalendar.startAnimation(animVisible)
        if (isVisible) {
            visibilityAllChildView(View.INVISIBLE)
            isVisible = false
        } else {
            visibilityAllChildView(View.VISIBLE)
            isVisible = true
        }
    }

    private fun visibilityAllChildView(visibilityState: Int) {
        for (i in 0..childCount - 2) {
            getChildAt(i).visibility = visibilityState
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initFloatingButton(fab: CustomFloatingButton, imgResource: Int, title: String) {
        //init parent layout
        val linearLayout = LinearLayout(context)
        val layoutParam = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 200)
        layoutParam.setMargins(10, 10, 10, 10)
        layoutParam.gravity = Gravity.END
        linearLayout.layoutParams = layoutParam
        linearLayout.setPadding(10, 10, 10, 10)
        linearLayout.visibility = View.INVISIBLE
        linearLayout.gravity = Gravity.CENTER

        // init fab
        fab.id = View.generateViewId()
        fab.setImageResource(imgResource)
        fab.background = resources.getDrawable(R.drawable.custom_floating_button, null)
        fab.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white, null))
//        fab.visibility = View.INVISIBLE
        fab.setOnClickListener(this)

        // init textView title
        val tvTitleLayoutParam = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT)
        tvTitleLayoutParam.marginEnd = 30
        val tvTitle = TextView(context)
        tvTitle.layoutParams = tvTitleLayoutParam
        tvTitle.text = title
        tvTitle.setPadding(0, 0, 20, 0)
        tvTitle.textSize = 16f
        tvTitle.background = resources.getDrawable(R.drawable.custom_textview, null)
        tvTitle.gravity = Gravity.CENTER
        tvTitle.visibility = View.GONE
        fab.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> tvTitle.visibility = View.VISIBLE
                MotionEvent.ACTION_UP -> tvTitle.visibility = View.GONE
            }
            false
        }

        // add fab and textView to layout
        linearLayout.addView(tvTitle)
        linearLayout.addView(fab)

        // add layout to root
        addView(linearLayout)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initFloatingMenu() {
        //common layout
        val layoutParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParam.setMargins(10, 10, 10, 10)
        layoutParam.gravity = Gravity.END

        //init floating button Menu
        mFabMenu.id = View.generateViewId()
        mFabMenu.background = resources.getDrawable(R.drawable.custom_floating_button_menu, null)
        mFabMenu.setImageResource(R.drawable.ic_menu)
        mFabMenu.layoutParams = layoutParam

        mFabMenu.setOnClickListener(this)
        addView(mFabMenu)
    }
}