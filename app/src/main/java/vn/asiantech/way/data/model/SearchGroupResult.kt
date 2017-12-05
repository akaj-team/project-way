package vn.asiantech.way.data.model

import com.google.gson.annotations.SerializedName

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 05/12/2017
 */
data class SearchGroupResult(@SerializedName("results") val groups: MutableList<Group>)
