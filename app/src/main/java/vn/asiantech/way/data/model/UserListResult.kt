package vn.asiantech.way.data.model

import com.google.gson.annotations.SerializedName
import com.hypertrack.lib.models.User

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 10/11/2017
 */
data class UserListResult(@SerializedName("results") val users: MutableList<User>)
