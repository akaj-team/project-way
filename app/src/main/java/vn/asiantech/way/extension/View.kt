package vn.asiantech.way.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by haingoq on 12/10/2017.
 */
fun View.hideKeyboard(context: Context) {
    val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}
