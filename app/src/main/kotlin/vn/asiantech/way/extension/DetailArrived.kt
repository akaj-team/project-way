package vn.asiantech.way.extension

import android.content.Context
import vn.asiantech.way.R

/**
 * Copyright © 2017 AsianTech inc.
 * Created by at-hoavo on 03/10/2017.
 */

private const val UNIT_MILLISECOND = 1000
private const val UNIT_METER = 1000
private const val UNIT_SECOND = 60
private const val UNIT_MINUTE = 60

/**
 * Make duration to string
 * @param context: Context
 * @return time to string
 */
internal fun Long.makeDuration(context: Context): String {
    var timer = ""
    var checkHour = false
    val hours = this / (UNIT_MILLISECOND * UNIT_MINUTE * UNIT_SECOND)
    val minutes = (this % (UNIT_MILLISECOND * UNIT_SECOND * UNIT_MINUTE)) /
            (UNIT_MILLISECOND * UNIT_SECOND)
    when (hours.compareTo(0)) {
        1 -> timer += hours.toString() + " " + context.getString(R.string.show_hour)
        0 -> checkHour = true
    }

    if (minutes > 0 || minutes == 0L && checkHour) {
        timer += " " + minutes.toString() + " " + context.getString(R.string.show_minutes)
    }
    return timer
}

/**
 * Make distance
 * @param context: Context
 * @return distance to kilometer
 */
internal fun Double.makeDistance(context: Context): String = (this / UNIT_METER).toInt().toString()
        .plus(" ").plus(context.getString(R.string.show_distance))

/**
 * Make average speed to string
 * @param context: Context
 * @return average speed to string
 */
internal fun Double.makeAverageSpeed(context: Context): String = this.toInt().toString()
        .plus(" ").plus(context.resources.getString(R.string.show_average_speed))
