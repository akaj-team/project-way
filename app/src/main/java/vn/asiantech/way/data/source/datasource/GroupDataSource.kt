package vn.asiantech.way.data.source.datasource

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 04/12/2017
 */
interface GroupDataSource {

    /**
     * This method used to create a group with a given name and ownerId.
     *
     * @param groupName - name of group will create.
     * @param ownerId - id of group owner.
     */
    fun createGroup(groupName: String, ownerId: String): Single<Boolean>

    /**
     * This method used to get info of a given group.
     *
     * @param groupId - id of given group.
     */
    fun getGroupInfo(groupId: String): Observable<Group>

    /**
     * This method used to get id of group that a given user have joined.
     *
     * @param userId - id of given user.
     */
    fun listenerForGroupChange(userId: String): Observable<String>

    /**
     * This method used to get invite list of a given user.
     *
     * @param userId - id of given user.
     */
    fun getInvite(userId: String): Observable<Invite>

    /**
     * This method used to get request list a given group.
     *
     * @param groupId - id of given group.
     */
    fun getGroupRequest(groupId: String): Observable<Invite>

    /**
     * This method used to up info of group to firebase database after create.
     *
     * @param group - upload group.
     */
    fun postGroupInfo(group: Group): Observable<Boolean>

    /**
     * This method used to change owner of a given group to a given user
     *
     * @param groupId - id of given group.
     * @param newOwner- id of given user.
     */
    fun changeGroupOwner(groupId: String, newOwner: String): Single<Boolean>

    /**
     * This method used to remove a given group.
     *
     * @param groupId - given group.
     */
    fun removeGroup(groupId: String): Observable<Boolean>

    /**
     * This method used to up a given invite to invite list a given user.
     *
     * @param userId - given user.
     * @param invite - given invite.
     */
    fun postInvite(userId: String, invite: Invite): Single<Boolean>

    /**
     * This method used to remove a given user from his/her group.
     *
     * @param userId - id of given user.
     */
    fun removeUserFromGroup(userId: String): Single<Boolean>

    /**
     * This method used to search group by name.
     *
     * @param groupName - name of group.
     */
    fun searchGroup(groupName: String): Observable<List<Group>>

    /**
     * This method used to get current request of a given user.
     *
     * @param userId - id of given user
     */
    fun getCurrentRequestOfUser(userId: String): Observable<Invite>

    /**
     * This method used to post a request to group.
     *
     * @param request - given request.
     */
    fun postRequestToGroup(groupId: String, request: Invite): Single<Boolean>

    /**
     * This method used to post a request to user.
     *
     * @param request - given request
     */
    fun postRequestToUser(userId: String, request: Invite): Single<Boolean>

    /**
     * This method used to search user by name.
     *
     * @param name - name used to search.
     */
    fun searchUser(name: String): Observable<List<User>>

    /**
     * This method used to delete a given invite when a given user refuse it.
     *
     * @param userId - id of given user.
     * @param invite - given invite.
     */
    fun deleteUserInvite(userId: String, invite: Invite): Single<Boolean>

    /**
     * This method used to delete a given request when group owner refuse it.
     *
     * @param groupId - id of given group.
     * @param invite - given request.
     */
    fun deleteGroupRequest(groupId: String, request: Invite): Single<Boolean>

    /**
     *
     */
    fun deleteCurrentRequestOfUserFromGroup(userId: String, request: Invite): Single<Boolean>

    /**
     * This method used to get user info by userId
     */
    fun getUserInfo(userId: String): Single<User>
}
