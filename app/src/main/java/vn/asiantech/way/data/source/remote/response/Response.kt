package vn.asiantech.way.data.source.remote.response

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 11/24/17.
 */
class Response<T> {
    var status: String = ""
    var message: String = ""
    var results: T? = null
}
