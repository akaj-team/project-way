package vn.asiantech.way.data.source

import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.remote.response.LocationResponse

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 11/24/17.
 */
internal interface WayDataSource {

    fun createUser(userParams:UserParams)

    fun updateUser(userParams:UserParams)

    fun getUser():Observable<User>

    fun getAddressLocation(latLng: String): Observable<LocationResponse>

    fun getLocationDetail(placeId: String?, key: String): Observable<ResultPlaceDetail>

    fun searchLocations(input: String,
                        key: String,
                        language: String = "vi",
                        sensor: Boolean = false): Observable<AutoCompleteResult>

    fun createGroup(name: String): Observable<Group>

    fun getGroupInfo(groupId: String): Observable<Group>

    fun getMembersList(groupId: String): Observable<UserListResult>

    fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User>

    fun removeUserFromGroup(userId: String, body: BodyAddUserToGroup): Observable<User>
}
