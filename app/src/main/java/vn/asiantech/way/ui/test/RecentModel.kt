package vn.asiantech.way.ui.test

import android.support.annotation.StringRes

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
data class RecentModel(var name: String, var description: String, var icon: String)

data class PopularModel(var name: String, var icon: String)

data class HeaderModel(@StringRes var name: Int)
