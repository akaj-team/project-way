package vn.asiantech.way.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 14/11/2017.
 */
class DateTimeUtil {

    internal fun getTimeChangeStatus(): String {
        val currentTime = Calendar.getInstance().time
        val formatDate = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return formatDate.format(currentTime)
    }
}
