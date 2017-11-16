package vn.asiantech.way.data.remote.hypertrackremote

import com.hypertrack.lib.models.User
import retrofit2.Call
import retrofit2.http.*
import vn.asiantech.way.data.model.group.BodyAddUserToGroup
import vn.asiantech.way.data.model.group.Group
import vn.asiantech.way.data.model.group.SearchGroupResult
import vn.asiantech.way.data.model.group.UserListResult

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
    fun createGroup(@Field("name") name: String): Call<Group>

    /**
     * This metod used to get information of a given group.
     *
     * @param groupId - id of given group.
     */
    @GET("groups/{groupId}/")
    fun getGroupInfo(@Path("groupId") groupId: String): Call<Group>

    /**
     *  This method used to get members list of given group.
     *
     *  @param groupId - id of given group.
     */
    @GET("users/")
    fun getMembersList(@Query("group_id") groupId: String): Call<UserListResult>

    /**
     *  This method used to search groups by name.
     *
     *  @param name - name of group.
     */
    @GET("groups/")
    fun searchGroup(@Query("name") name: String): Call<SearchGroupResult>

    /**
     * This method used to add a user to given group.
     *
     * @param userId - id of  user you wanna add.
     * @param body - data contain id of given group.
     */
    @PATCH("users/{userId}/")
    fun addUserToGroup(@Path("userId") userId: String, @Body body: BodyAddUserToGroup): Call<User>

    /**
     * This method used to remove a user from given group.
     *
     * @param userId - id of  user you wanna delete.
     * @param body - data contain id of given group.
     */
    @PATCH("users/{userId}/")
    fun removeUserFromGroup(@Path("userId") userId: String,
                            @Body body: BodyAddUserToGroup): Call<User>
}
