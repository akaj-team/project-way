package vn.asiantech.way.data.source

import com.google.android.gms.maps.model.LatLng
import com.hypertrack.lib.internal.common.models.VehicleType
import com.hypertrack.lib.models.*
import io.reactivex.Observable
import io.reactivex.Single
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.datasource.WayDataSource
import vn.asiantech.way.data.source.remote.WayRemoteDataSource
import vn.asiantech.way.data.source.remote.response.ResponseStatus

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 11/24/17.
 */
class WayRepository : WayDataSource {
    private val remoteDataSource = WayRemoteDataSource()

    override fun createUser(userParams: UserParams): Observable<ResponseStatus>
            = remoteDataSource.createUser(userParams)

    override fun updateUser(userParams: UserParams): Observable<ResponseStatus>
            = remoteDataSource.updateUser(userParams)

    override fun getUser(): Single<User> = remoteDataSource.getUser()

    override fun getAddressLocation(latLng: String): Observable<MutableList<LocationAddress>>
            = remoteDataSource.getAddressLocation(latLng)

    override fun getLocationDetail(placeId: String?): Observable<ResultPlaceDetail>
            = remoteDataSource.getLocationDetail(placeId)

    override fun searchLocations(input: String, language: String, sensor: Boolean): Observable<AutoCompleteResult>
            = remoteDataSource.searchLocations(input, language, sensor)

    override fun createGroup(name: String): Observable<Group> = remoteDataSource.createGroup(name)

    override fun getGroupInfo(groupId: String): Observable<Group> = remoteDataSource.getGroupInfo(groupId)

    override fun getGroupMembers(groupId: String): Observable<MutableList<User>>
            = remoteDataSource.getGroupMembers(groupId)

    override fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User>
            = remoteDataSource.addUserToGroup(userId, body)

    override fun removeUserFromGroup(userId: String, body: BodyAddUserToGroup): Observable<User>
            = remoteDataSource.removeUserFromGroup(userId, body)

    override fun getCurrentLocation(): Observable<HyperTrackLocation>
            = remoteDataSource.getCurrentLocation()

    override fun getETA(destination: LatLng, vehicle: VehicleType): Observable<Float>
            = remoteDataSource.getETA(destination, vehicle)

    override fun createAndAssignAction(builder: ActionParamsBuilder): Observable<Action>
            = remoteDataSource.createAndAssignAction(builder)
}
