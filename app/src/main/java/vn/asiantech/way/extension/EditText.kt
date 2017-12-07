package vn.asiantech.way.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 *
 * Created by haingoq on 06/12/2017.
 */
fun EditText.onUserInformationChange(onTextChanged: (CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) = Unit

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged.invoke(p0)
        }
    })
}
