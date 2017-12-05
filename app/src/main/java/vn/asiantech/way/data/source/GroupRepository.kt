package vn.asiantech.way.data.source

import io.reactivex.Observable
import io.reactivex.Single
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
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

    override fun getInvite(userId: String): Observable<Invite> {
        return remoteDataSource.getInvite(userId)
    }

    override fun getGroupRequest(groupId: String): Observable<Invite> {
        return remoteDataSource.getGroupRequest(groupId)
    }

    override fun postGroupInfo(group: Group): Observable<Boolean> {
        return remoteDataSource.postGroupInfo(group)
    }

    override fun changeGroupOwner(groupId: String, newOwner: String): Single<Boolean> {
        return remoteDataSource.changeGroupOwner(groupId, newOwner)
    }

    override fun removeGroup(groupId: String): Observable<Boolean> {
        return remoteDataSource.removeGroup(groupId)
    }

    override fun postInvite(userId: String, invite: Invite): Single<Boolean> {
        return remoteDataSource.postInvite(userId, invite)
    }

    override fun removeUserFromGroup(userId: String): Single<Boolean> {
        return remoteDataSource.removeUserFromGroup(userId)
    }

    override fun searchGroup(groupName: String): Observable<List<Group>> {
        return remoteDataSource.searchGroup(groupName)
    }
}
