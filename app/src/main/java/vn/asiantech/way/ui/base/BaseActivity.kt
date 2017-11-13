package vn.asiantech.way.ui.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 9/21/17.
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val SHARED_NAME = "shared"
        const val KEY_LOGIN = "login"
        const val KEY_LOCAL_USER = "local_user"
    }

    lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedPreferences = getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }
}
