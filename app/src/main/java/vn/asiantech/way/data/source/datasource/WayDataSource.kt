package vn.asiantech.way.data.source.datasource

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.hypertrack.lib.internal.common.models.VehicleType
import com.hypertrack.lib.models.*
import io.reactivex.Observable
import io.reactivex.Single
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
    fun getUser(): Single<User>

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
    fun getLocationDetail(placeId: String?): Observable<ResultPlaceDetail>

    /**
     *  This method to search location by name.
     *
     *  @param input the query to search location.
     *  @param key the api key of google place api.
     *  @return AutoCompleteResult object which is Observable.
     */
    fun searchLocations(input: String,
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

    /**
     *  Get current location on Map.
     *
     *  @return HyperTrackLocation object which is Observable.
     */
//    fun getCurrentLocation(): Observable<HyperTrackLocation>

    /**
     *  Get ETA(Estimated Time of Arrival) of user
     *
     *  @param destination: information of position.
     *  @param vehicle: type of vehicle
     *  @return Float object which is Observable.
     */
    fun getETA(destination: LatLng, vehicle: VehicleType): Observable<Float>

    /**
     *  Create and  assign action
     *
     *  @param builder: necessary information to create Action
     *  @return Action object which is Observable.
     */
    fun createAndAssignAction(builder: ActionParamsBuilder): Observable<Action>

    /**
     * This method return ETA distance and ETA duration.
     *
     *  @param origin the start LatLng
     *  @param destination the destinations LatLng*
     *  @return ResultDistance object which is Observable.
     */
    fun getLocationDistance(origin: String, destination: String): Observable<ResultDistance>

    /**
     * This method return list LatLng from 2 point.
     *
     *  @param url the url of google road api.
     *  @return ResultRoad object which is Observable.
     */
    fun getListLocation(url: String): Observable<ResultRoad>

    /**
     * This method return the url for sharing your location
     *
     * @return String url which is Observable
     */
    fun getTrackingURL(): Single<String>

    /**
     * This method return the location name for sharing your location
     *
     * @param context the context
     * @param latLng the location LatLng
     * @return String location name which is Observable
     */
    fun getLocationName(context: Context, latLng: LatLng): Single<String>

    /**
     * This method return HyperTrack current location
     *
     * @return HyperTrackLocation object which is Observable
     */
    fun getCurrentHyperTrackLocation(): Single<HyperTrackLocation>

    /**
     * This method return Location
     *
     * @param context the context
     * @return Location object which is Observable
     */
    fun getCurrentLocation(context: Context): Single<Location>
}
