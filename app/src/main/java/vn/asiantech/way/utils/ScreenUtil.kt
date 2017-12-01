package vn.asiantech.way.utils

import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 01/12/2017.
 */
class ScreenUtil {

    companion object {
        private const val TYPE_UNIT_DIALOG_HEIGHT = 5
        private const val TYPE_DIALOG_MARGIN_WIDTH = 80

        internal fun getScreenSize(context: Context, dialog: Dialog) {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            val windowParams = WindowManager.LayoutParams()
            windowParams.copyFrom(dialog.window.attributes)
            windowParams.height = height - height / TYPE_UNIT_DIALOG_HEIGHT
            windowParams.width = width - TYPE_DIALOG_MARGIN_WIDTH
            dialog.window.attributes = windowParams
        }
    }
}
