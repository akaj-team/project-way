package vn.asiantech.way.ui.view

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout


/**
 * Custom floating button
 * Created by haingoq on 29/09/2017.
 */
class CustomFloatingButton : FloatingActionButton, View.OnClickListener {
    val mLayout: LinearLayout = LinearLayout(context)

    constructor(context: Context) : super(context) {
        setOnClickListener(this)
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        setOnClickListener(this)
        mLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onClick(p0: View?) {
        Log.d("xxx", "xxxxxx")
    }
}
