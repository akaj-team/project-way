package vn.asiantech.way.data.model.group

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 17/11/2017
 */
data class Invite(var from: String, var to: String, var groupName: String,
                  var request: Boolean = false)