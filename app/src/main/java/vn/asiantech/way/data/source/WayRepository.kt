package vn.asiantech.way.data.source

import android.content.Context
import android.location.Location
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

    override fun getLocationDistance(origin: String, destination: String) =
            remoteDataSource.getLocationDistance(origin, destination)

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

    override fun getETA(destination: LatLng, vehicle: VehicleType): Observable<Float>
            = remoteDataSource.getETA(destination, vehicle)

    override fun getListLocation(url: String): Observable<ResultRoad> {
        return remoteDataSource.getListLocation(url)
    }

    override fun createAndAssignAction(builder: ActionParamsBuilder): Observable<Action>
            = remoteDataSource.createAndAssignAction(builder)

    override fun getTrackingURL(): Single<String> = remoteDataSource.getTrackingURL()

    override fun getLocationName(context: Context, latLng: LatLng): Single<String> = remoteDataSource.getLocationName(context, latLng)

    override fun getCurrentLocationHyperTrack(): Single<HyperTrackLocation> =
            remoteDataSource.getCurrentLocationHyperTrack()

    override fun getCurrentLocation(context: Context): Single<Location> =
            remoteDataSource.getCurrentLocation(context)
}
