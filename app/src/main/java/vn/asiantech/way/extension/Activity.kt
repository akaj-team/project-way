package vn.asiantech.way.extension

import android.app.Activity
import android.widget.Toast

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 27/09/2017.
 */

/**
 * Toast message
 * @param message: CharSequence to show on toast
 * @param duration: Int, default is Toast.LENGTH_SHORT
 */
internal fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
