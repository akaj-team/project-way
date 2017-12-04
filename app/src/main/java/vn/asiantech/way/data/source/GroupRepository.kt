package vn.asiantech.way.data.source

import io.reactivex.Observable
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.datasource.GroupDataSource
import vn.asiantech.way.data.source.remote.GroupRemoteDataSource

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 04/12/2017
 */
class GroupRepository : GroupDataSource {

    private val remoteDataSource = GroupRemoteDataSource()

    override fun getGroupId(userId: String): Observable<String> {
        return remoteDataSource.getGroupId(userId)
    }

    override fun getGroupInfo(groupId: String): Observable<Group> {
        return remoteDataSource.getGroupInfo(groupId)
    }
}
