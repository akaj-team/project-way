package vn.asiantech.way.data.source.remote.response

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 11/24/17.
 */
class WayResponse<T> {
    var resultCode: Int = -1
    var message: String = ""
    var data: T? = null
}
