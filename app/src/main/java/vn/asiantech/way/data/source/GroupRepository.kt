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

    override fun listenerForGroupChange(userId: String): Observable<String> = remoteDataSource.listenerForGroupChange(userId)

    override fun getGroupInfo(groupId: String): Observable<Group> = remoteDataSource.getGroupInfo(groupId)

    override fun getInvite(userId: String): Observable<Invite> = remoteDataSource.getInvite(userId)

    override fun getGroupRequest(groupId: String): Observable<Invite> = remoteDataSource.getGroupRequest(groupId)

    override fun postGroupInfo(group: Group): Single<Boolean> = remoteDataSource.postGroupInfo(group)

    override fun changeGroupOwner(groupId: String, newOwner: String): Single<Boolean>
            = remoteDataSource.changeGroupOwner(groupId, newOwner)

    override fun removeGroup(groupId: String): Observable<Boolean> = remoteDataSource.removeGroup(groupId)

    override fun postInvite(userId: String, invite: Invite): Single<Boolean> = remoteDataSource.postInvite(userId, invite)

    override fun removeUserFromGroup(userId: String): Single<User> = remoteDataSource.removeUserFromGroup(userId)

    override fun searchGroup(groupName: String): Observable<List<Group>> = remoteDataSource.searchGroup(groupName)

    override fun getCurrentRequestOfUser(userId: String): Observable<Invite> = remoteDataSource.getCurrentRequestOfUser(userId)

    override fun postRequestToGroup(groupId: String, request: Invite): Single<Boolean>
            = remoteDataSource.postRequestToGroup(groupId, request)

    override fun postRequestToUser(userId: String, request: Invite): Single<Boolean>
            = remoteDataSource.postRequestToGroup(userId, request)

    override fun searchUser(name: String): Observable<List<User>> = remoteDataSource.searchUser(name)

    override fun deleteGroupRequest(groupId: String, request: Invite): Single<Boolean>
            = remoteDataSource.deleteGroupRequest(groupId, request)

    override fun deleteUserInvite(userId: String, invite: Invite): Single<Boolean>
            = remoteDataSource.deleteUserInvite(userId, invite)

    override fun deleteCurrentRequestOfUserFromGroup(userId: String, request: Invite): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createGroup(groupName: String, ownerId: String): Single<Boolean> = remoteDataSource.createGroup(groupName, ownerId)

    override fun getMemberList(groupId: String): Single<MutableList<User>> = remoteDataSource.getMemberList(groupId)

    override fun acceptInvite(userId: String, invite: Invite): Single<Boolean> = remoteDataSource.acceptInvite(userId, invite)

    override fun getUserInfo(userId: String): Single<User> = remoteDataSource.getUserInfo(userId)
}
