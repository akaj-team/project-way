package vn.asiantech.way.data.model

import java.io.Serializable

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 09/11/2017
 */
data class Group(val id: String, var name: String, var token: String,
                 var ownerId: String, val created_at: String, var modified_at: String) : Serializable
