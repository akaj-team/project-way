package vn.asiantech.way.extensions

import android.content.Context
import android.content.res.Resources
import vn.asiantech.way.R

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 03/10/2017.
 */
fun Long.makeDuration(context: Context): String {
    var timer = ""
    val hours = (this / (1000 * 60 * 60)).toInt()
    val minutes = (this % (1000 * 60 * 60)).toInt() / (1000 * 60)
    if (hours > 0) {
        timer += hours.toString() + Resources.getSystem().getString(R.string.show_hour)
    } else
        timer += ""

    if (minutes > 0) {
        timer += minutes.toString()
    } else timer += 0
    return timer + context.resources.getString(R.string.show_minutes)
}

fun Double.makeDistance(context: Context): String {
    return (this / 1000).toInt().toString() + context.resources.getString(R.string.show_distance)
}

fun Double.makeAverageSpeed(context: Context): String {
    return this.toInt().toString() + context.resources.getString(R.string.show_average_speed)
}
