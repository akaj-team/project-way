package vn.asiantech.way.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Created by ngoctrant.n on 12/7/17.
 * Extension method on text change
 */

fun EditText.onUserNameChanged(onTextChanged: (CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) = Unit
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged.invoke(p0)
        }
    })
}
