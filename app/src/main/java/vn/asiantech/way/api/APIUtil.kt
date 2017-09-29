package vn.asiantech.way.api

/**
 * Copyright © AsianTech Co., Ltd
 * Created by toan on 29/09/2017.
 */
class APIUtil {
    companion object {
        fun getService(): ServiceApi? = RetrofitClient.getClient()?.create(ServiceApi::class.java)
    }
}