package vn.asiantech.way.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 09/11/2017
 */
data class Group(val id: String, var name: String, var token: String,
                 val ownerId: String, @SerializedName("created_at") val createAt: String,
                 @SerializedName("modified_at") var modifiedAt: String) : Serializable
