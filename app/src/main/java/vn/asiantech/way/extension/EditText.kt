package vn.asiantech.way.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Copyright © 2017 AsianTech inc.
 * Created by haingoq on 06/12/2017.
 * Function for EditText when text changed
 */
fun EditText.onTextChangeListener(onTextChanged: (CharSequence?) -> Unit = {},
                                  afterTextChanged: (Editable?) -> Unit = {},
                                  beforeTextChanged: (CharSequence?) -> Unit = {}) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            afterTextChanged.invoke(p0)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            beforeTextChanged.invoke(p0)
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged.invoke(p0)
        }
    })
}
