package vn.asiantech.way.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * ScreenUtil
 *
 * @author at-ToanNguyen on 27/11/2017
 */
class ScreenUtil private constructor() {
    companion object {
        /**
         * This method is used to get width of screen
         *
         * @param context is current context
         * @return return height screen in pixel
         */
        fun getWidthScreen(context: Context): Int {
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val dimension = DisplayMetrics()
            wm?.defaultDisplay?.getMetrics(dimension)
            return dimension.widthPixels
        }

        /**
         * This method is used to get height of screen
         *
         * @param context is current context
         * @return return height screen in pixel
         */
        fun getHeightScreen(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val dimension = DisplayMetrics()
            wm?.defaultDisplay?.getMetrics(dimension)
            return dimension.heightPixels
        }
    }
}
