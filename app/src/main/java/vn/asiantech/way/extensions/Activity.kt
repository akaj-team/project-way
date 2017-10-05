package vn.asiantech.way.extensions

import android.app.Activity
import android.widget.Toast

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 27/09/2017.
 */
fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
