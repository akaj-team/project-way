package vn.asiantech.way.data.source.datasource

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
    fun getGroupId(userId: String): Observable<String>

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
    fun getRequest(groupId: String): Observable<Invite>

    /**
     * This method used to up info of group to firebase database after create.
     *
     * @param group - upload group.
     */
    fun upGroupInfo(group: Group): Observable<Boolean>

    /**
     * This method used to change owner of a given group to a given user
     *
     * @param groupId - id of given group.
     * @param newOwner- id of given user.
     */
    fun changeOwner(groupId: String, newOwner: String): Observable<Boolean>

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
    fun upInvite(userId: String, invite: Invite): Single<Boolean>

    /**
     * This method used to remove a given user from his/her group.
     *
     * @param userId - id of given user.
     */
    fun removeUserFromGroup(userId: String): Single<Boolean>
}