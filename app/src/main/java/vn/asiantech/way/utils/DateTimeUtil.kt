package vn.asiantech.way.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 14/11/2017.
 */
class DateTimeUtil {

    internal fun getTimeChangeStatus(): String {
        val currentTime = Calendar.getInstance().time
        val formatDate = SimpleDateFormat("hh:mm a")
        return formatDate.format(currentTime)
    }
}
