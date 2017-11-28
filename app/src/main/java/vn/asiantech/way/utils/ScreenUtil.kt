package vn.asiantech.way.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * ScreenUtil.
 *
 * @author at-ToanNguyen
 */
object ScreenUtil {
    fun getWidthScreen(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dimension = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dimension)
        return dimension.widthPixels
    }

    fun getHeightScreen(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dimension = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dimension)
        return dimension.heightPixels
    }
}