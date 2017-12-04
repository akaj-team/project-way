package vn.asiantech.way.ui.register

import android.text.Editable
import android.text.TextWatcher

/**
 *
 * Created by haingoq on 04/12/2017.
 */
interface OnWayTextChangeListener : TextWatcher {
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
    override fun afterTextChanged(p0: Editable?) = Unit
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
}
