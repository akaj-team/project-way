package vn.asiantech.way.data.source.remote

import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.WayDataSource
import vn.asiantech.way.data.source.remote.response.LocationResponse

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 11/24/17.
 */
internal class WayRemoteDataSource : WayDataSource {

    override fun getUser(): Observable<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUser(userParams: UserParams) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUser(userParams: UserParams) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAddressLocation(latLng: String): Observable<LocationResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocationDetail(placeId: String?, key: String): Observable<ResultPlaceDetail> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchLocations(input: String, key: String, language: String, sensor: Boolean): Observable<AutoCompleteResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createGroup(name: String): Observable<Group> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupInfo(groupId: String): Observable<Group> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMembersList(groupId: String): Observable<UserListResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeUserFromGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
