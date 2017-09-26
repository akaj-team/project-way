package vn.asiantech.way.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by haibt on 9/26/17.
 */
class Utils {
    companion object {
        /**
         * method to hide keyboard
         */
        fun hideKeyboard(context: Context, view: View) {
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
