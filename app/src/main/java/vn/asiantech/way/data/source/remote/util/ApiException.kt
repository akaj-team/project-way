package vn.asiantech.way.data.source.remote.util

/**
 * Use this file to handle error from api
 */
class ApiException(message: String?) : Throwable(message) {
    lateinit var errorCode: String
    lateinit var errorMessage: String
}
