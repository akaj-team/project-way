package vn.asiantech.way.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 07/12/2017.
 *  Function for EditText after text changed
 */
internal fun EditText.afterTextChanged(afterTextChanged: (Editable?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })
}
