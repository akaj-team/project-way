package vn.asiantech.way.ui.view

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import vn.asiantech.way.R


/**
 * Created by haingoq on 29/09/2017.
 */
class FloatingButtonMenu : LinearLayout, View.OnClickListener {
    val mImgMenu: CircleImageView = CircleImageView(context)
    val mImgShare: CircleImageView = CircleImageView(context)
    val mImgProfile: CircleImageView = CircleImageView(context)
    val mImgCalendar: CircleImageView = CircleImageView(context)
    var isVisible: Boolean = false
    val mMargin: Int = resources.getDimensionPixelSize(R.dimen.margin)

    constructor(context: Context) : super(context) {
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        initMenuItem(mImgCalendar, R.drawable.ic_profile, "Calendar")
        initMenuItem(mImgProfile, R.drawable.ic_profile, "Profile")
        initMenuItem(mImgShare, R.drawable.ic_share, "Share")
        initFloatingMenu()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {
        when (view?.id) {
            mImgMenu.id -> {
                val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
                mImgMenu.startAnimation(anim)
                val animVisible: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_alpha)
                mImgShare.startAnimation(animVisible)
                mImgProfile.startAnimation(animVisible)
                mImgCalendar.startAnimation(animVisible)
                if (isVisible) {
                    visibilityAllChildView(View.INVISIBLE)
                    isVisible = false
                } else {
                    visibilityAllChildView(View.VISIBLE)
                    isVisible = true
                }
            }
        }
    }

    private fun visibilityAllChildView(visibilityState: Int) {
        for (i in 0..childCount - 2) {
            getChildAt(i).visibility = visibilityState
        }
    }

    private fun initMenuItem(imageView: CircleImageView, imgResource: Int, title: String) {
        //init parent layout
        val height: Int = resources.getDimensionPixelSize(R.dimen.layout_item_height)
        val linearLayout = LinearLayout(context)
        val layoutParam = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height)
        layoutParam.setMargins(mMargin, mMargin, mMargin, mMargin)
        layoutParam.gravity = Gravity.END
        linearLayout.layoutParams = layoutParam
        linearLayout.setPadding(mMargin, mMargin, mMargin, mMargin)
        linearLayout.visibility = View.INVISIBLE
        linearLayout.gravity = Gravity.CENTER

        // init imageView
//        imageView.id = View.generateViewId()
        val imageViewParam = LinearLayout.LayoutParams(height, height)
        imageView.layoutParams = imageViewParam
        imageView.setImageResource(imgResource)
        imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
        imageView.setOnClickListener(this)

        // init textView title
        val tvTitleLayoutParam = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT)
        tvTitleLayoutParam.setMargins(0, 0, mMargin, 0)
        val tvTitle = TextView(context)
        tvTitle.layoutParams = tvTitleLayoutParam
        tvTitle.text = title
        tvTitle.setPadding(0, 0, mMargin, 0)
        tvTitle.textSize = 16f
        tvTitle.background = ContextCompat.getDrawable(context, R.drawable.custom_textview)
        tvTitle.gravity = Gravity.CENTER
        tvTitle.visibility = View.GONE
        imageView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> tvTitle.visibility = View.VISIBLE
                MotionEvent.ACTION_UP -> tvTitle.visibility = View.GONE
            }
            false
        }

        // add fab and textView to layout
        linearLayout.addView(tvTitle)
        linearLayout.addView(imageView)

        // add layout to root
        addView(linearLayout)
    }

    private fun initFloatingMenu() {
        //  create layout param
        val sizeLayout: Int = resources.getDimensionPixelSize(R.dimen.layout_menu_height)
        val layoutParam = LinearLayout.LayoutParams(sizeLayout, sizeLayout)
        layoutParam.setMargins(mMargin, mMargin, mMargin, mMargin)
        layoutParam.gravity = Gravity.END

        //  init floating button Menu
//        mImgMenu.id = View.generateViewId()
        mImgMenu.setImageResource(R.drawable.ic_menu)
        mImgMenu.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
        mImgMenu.layoutParams = layoutParam
        mImgMenu.setOnClickListener(this)
        addView(mImgMenu)
    }
}
