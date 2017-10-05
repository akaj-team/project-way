package vn.asiantech.way.api

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
class APIUtil {
    companion object {
        /**
         * function use get Service
         */
        internal fun getService(): ServiceApi? = RetrofitClient.getClient()?.create(ServiceApi::class.java)
    }
}
