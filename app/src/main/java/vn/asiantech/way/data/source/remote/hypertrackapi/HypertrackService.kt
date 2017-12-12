package vn.asiantech.way.data.source.remote.hypertrackapi

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.SearchGroupResult
import vn.asiantech.way.data.model.UserListResult
import vn.asiantech.way.data.source.remote.response.Response

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 09/11/2017
 */
interface HypertrackService {

    /**
     * This method used to call api create group.
     *
     * @param name - name of group you wanna create.
     */
    @POST("groups/")
    @FormUrlEncoded
    fun createGroup(@Field("name") name: String): Single<Group>

    /**
     * This metod used to get information of a given group.
     *
     * @param groupId - id of given group.
     */
    @GET("groups/{groupId}/")
    fun getGroupInfo(@Path("groupId") groupId: String): Single<Group>

    /**
     *  This method used to get members list of given group.
     *
     *  @param groupId - id of given group.
     */
    @GET("users/")
    fun getGroupMembers(@Query("group_id") groupId: String): Single<Response<MutableList<User>>>

    /**
     * This method used to add a user to given group.
     *
     * @param userId - id of  user you wanna add.
     * @param body - data contain id of given group.
     */
    @PATCH("users/{userId}/")
    fun addUserToGroup(@Path("userId") userId: String, @Body body: BodyAddUserToGroup): Single<User>

    /**
     * This method used to remove a user from given group.
     *
     * @param userId - id of  user you wanna delete.
     * @param body - data contain id of given group.
     */
    @PATCH("users/{userId}/")
    fun removeUserFromGroup(@Path("userId") userId: String, @Body body: BodyAddUserToGroup): Single<User>

    /**
     *  This method used to search groups by name.
     *
     *  @param name - name of group.
     */
    @GET("groups/")
    fun searchGroup(@Query("name") name: String): Single<SearchGroupResult>

    /**
     * This method used to search users by name.
     *
     * @param name - query to search.
     */
    @GET("users/")
    fun searchUser(@Query("name") name: String): Single<UserListResult>

    /**
     * This method used to get user info by user id.
     */
    @GET("users/{userId}")
    fun getUserInfo(@Path("userId") userId: String): Observable<User>
}
