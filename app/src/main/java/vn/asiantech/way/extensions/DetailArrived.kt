package vn.asiantech.way.extensions

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 03/10/2017.
 */
fun Long.makeDuration(): String {
    var timer = ""
    val hours = (this / (1000 * 60 * 60)).toInt()
    val minutes = (this % (1000 * 60 * 60)).toInt() / (1000 * 60)
    if (hours > 0) {
        timer += hours.toString() + "h"
    } else
        timer += ""

    if (minutes > 0) {
        timer += minutes.toString()
    } else timer += 0

    return timer + " min "
}

fun Double.makeDistance(): String {
    return (this / 1000).toInt().toString() + " km "
}

fun Double.makeAverageSpeed(): String {
    return this.toInt().toString() + " kmph"
}
