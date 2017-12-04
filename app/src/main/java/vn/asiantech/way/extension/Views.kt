package vn.asiantech.way.extension

import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.hypertrack.lib.internal.consumer.view.RippleView
import org.jetbrains.anko.custom.ankoView
import vn.asiantech.way.utils.AppConstants

/**
 * Created by haingoq on 12/10/2017.
 * Extension method to hide keyboard.
 */
fun View.hideKeyboard(context: Context) {
    val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(this.windowToken, 0)
}

internal fun ViewManager.rippleView(init: RippleView.() -> Unit): RippleView {
    return ankoView(
            { RippleView(it, null) }, 0, init)
}

internal fun Dialog.setDialogScreenSize(context: Context) {
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels
    val windowParams = WindowManager.LayoutParams()
    windowParams.copyFrom(this.window.attributes)
    windowParams.height = height - height / AppConstants.TYPE_UNIT_DIALOG_HEIGHT
    windowParams.width = width - AppConstants.TYPE_DIALOG_MARGIN_WIDTH
    this.window.attributes = windowParams
}
