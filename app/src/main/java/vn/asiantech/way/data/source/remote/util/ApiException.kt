package vn.asiantech.way.data.source.remote.util

class ApiException(message: String?) : Throwable(message) {
    lateinit var errorCode: String
    lateinit var errorMessage: String
}
