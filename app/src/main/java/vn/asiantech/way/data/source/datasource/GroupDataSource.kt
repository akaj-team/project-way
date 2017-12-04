package vn.asiantech.way.data.source.datasource

import io.reactivex.Observable
import vn.asiantech.way.data.model.Group

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
    fun getInvite(userId: String): Observable<String> {

    }
}