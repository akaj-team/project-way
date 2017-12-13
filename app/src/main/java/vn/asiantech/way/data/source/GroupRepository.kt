package vn.asiantech.way.data.source

import com.hypertrack.lib.models.User
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

    override fun listenerForGroupChange(userId: String): Observable<String> {
        return remoteDataSource.listenerForGroupChange(userId)
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

    override fun postGroupInfo(group: Group): Single<Boolean> {
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

    override fun removeUserFromGroup(userId: String): Single<User> {
        return remoteDataSource.removeUserFromGroup(userId)
    }

    override fun searchGroup(groupName: String): Observable<List<Group>> {
        return remoteDataSource.searchGroup(groupName)
    }

    override fun getCurrentRequestOfUser(userId: String): Observable<Invite> {
        return remoteDataSource.getCurrentRequestOfUser(userId)
    }

    override fun postRequestToGroup(groupId: String, request: Invite): Single<Boolean> {
        return remoteDataSource.postRequestToGroup(groupId, request)
    }

    override fun postRequestToUser(userId: String, request: Invite): Single<Boolean> {
        return remoteDataSource.postRequestToGroup(userId, request)
    }

    override fun searchUser(name: String): Observable<List<User>> {
        return remoteDataSource.searchUser(name)
    }

    override fun deleteGroupRequest(groupId: String, request: Invite): Single<Boolean> {
        return remoteDataSource.deleteGroupRequest(groupId, request)
    }

    override fun deleteUserInvite(userId: String, invite: Invite): Single<Boolean> {
        return remoteDataSource.deleteUserInvite(userId, invite)
    }

    override fun deleteCurrentRequestOfUserFromGroup(userId: String, request: Invite): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createGroup(groupName: String, ownerId: String): Single<Boolean> {
        return remoteDataSource.createGroup(groupName, ownerId)
    }

    override fun getMemberList(groupId: String): Single<MutableList<User>> {
        return remoteDataSource.getMemberList(groupId)
    }

    override fun acceptInvite(userId: String, invite: Invite): Single<Boolean> {
        return remoteDataSource.acceptInvite(userId, invite)
    }

    override fun getUserInfo(groupId: String): Observable<User> {
        return remoteDataSource.getUserInfo(groupId)
    }
}
