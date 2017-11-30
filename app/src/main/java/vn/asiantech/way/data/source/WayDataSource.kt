package vn.asiantech.way.data.source

import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.remote.response.ResponseStatus

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 11/24/17.
 */
internal interface WayDataSource {

    /**
     *  This method to create new User.
     *
     *  @param userParams contain user information for Hypertrack to create new user
     *  @return ResponseStatus object which is Observable.
     */
    fun createUser(userParams: UserParams): Observable<ResponseStatus>

    /**
     *  This method to update User.
     *
     *  @param userParams: contain user information for Hypertrack to update.
     *  @return ResponseStatus object which is Observable.
     */
    fun updateUser(userParams: UserParams): Observable<ResponseStatus>

    /**
     *  Get information user.
     *  @param userId of user
     *  @return User object which is Observable.
     */
    fun getUser(): Observable<User>

    /**
     *  Get address at location on Map.
     *
     *  @param latLng: information of position.
     *  @return MutableList<LocationAddress> which is Observable.
     */
    fun getAddressLocation(latLng: String): Observable<MutableList<LocationAddress>>

    /**
     *  This method to get detail of location.
     *
     *  @param placeId the id of location.
     *  @return ResultPlaceDetail object which is Observable.
     */
    fun getLocationDetail(placeId: String?, key: String): Observable<ResultPlaceDetail>

    /**
     *  This method to search location by name.
     *
     *  @param input the query to search location.
     *  @param key the api key of google place api.
     *  @return AutoCompleteResult object which is Observable.
     */
    fun searchLocations(input: String,
                        key: String,
                        language: String = "vi",
                        sensor: Boolean = false): Observable<AutoCompleteResult>

    /**
     * This method used to call api create group.
     *
     * @param name - name of group you wanna create.
     * @return Group object which is Observable.
     */
    fun createGroup(name: String): Observable<Group>

    /**
     * This metod used to get information of a given group.
     *
     * @param groupId - id of given group.
     * @return Group object which is Observable.
     */
    fun getGroupInfo(groupId: String): Observable<Group>

    /**
     *  This method used to get members list of given group.
     *
     *  @param groupId - id of given group.
     *  @return MutableList<User> which is Observable.
     */
    fun getGroupMembers(groupId: String): Observable<MutableList<User>>

    /**
     * This method used to add a user to given group.
     *
     * @param userId - id of  user you wanna add.
     * @param body - data contain id of given group.
     * @return User object which is Observable.
     */
    fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User>

    /**
     * This method used to remove a user from given group.
     *
     * @param userId - id of  user you wanna delete.
     * @param body - data contain id of given group.
     * @return User object which is Observable.
     */
    fun removeUserFromGroup(userId: String, body: BodyAddUserToGroup): Observable<User>
}
