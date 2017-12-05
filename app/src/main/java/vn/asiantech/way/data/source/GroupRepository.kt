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

    override fun getRequest(groupId: String): Observable<Invite> {
        TODO("not implemented")
    }

    override fun upGroupInfo(group: Group): Observable<Boolean> {
        return remoteDataSource.upGroupInfo(group)
    }

    override fun changeOwner(groupId: String, newOwner: String): Observable<Boolean> {
        TODO("not implemented")
    }

    override fun removeGroup(groupId: String): Observable<Boolean> {
        TODO("not implemented")
    }

    override fun upInvite(userId: String, invite: Invite): Single<Boolean> {
        TODO("not implemented")
    }

    override fun removeUserFromGroup(userId: String): Single<Boolean> {
        TODO("not implemented")
    }
}
