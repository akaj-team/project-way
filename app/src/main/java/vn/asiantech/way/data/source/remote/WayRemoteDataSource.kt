package vn.asiantech.way.data.source.remote

import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.models.ErrorResponse
import com.hypertrack.lib.models.SuccessResponse
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.subjects.AsyncSubject
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.WayDataSource
import vn.asiantech.way.data.source.remote.googleapi.ApiClient
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackApi
import vn.asiantech.way.data.source.remote.response.ResponseStatus

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 11/24/17.
 */
internal class WayRemoteDataSource : WayDataSource {
    override fun createUser(userParams: UserParams): Observable<ResponseStatus> {
        val result = AsyncSubject.create<ResponseStatus>()


        HyperTrack.getOrCreateUser(userParams, object : HyperTrackCallback() {
            override fun onSuccess(success: SuccessResponse) {
                val res = ResponseStatus(true, "Success")
                result.onNext(res)
                result.onComplete()
            }

            override fun onError(error: ErrorResponse) {
                val res = Throwable(error.errorMessage)
                result.onError(res)
            }
        })
        return result
    }

    override fun updateUser(userParams: UserParams): Observable<ResponseStatus> {
        val result = AsyncSubject.create<ResponseStatus>()
        HyperTrack.updateUser(userParams, object : HyperTrackCallback() {
            override fun onSuccess(success: SuccessResponse) {
                val res = ResponseStatus(true, "Success")
                result.onNext(res)
                result.onComplete()
            }

            override fun onError(error: ErrorResponse) {
                val res = Throwable(error.errorMessage)
                result.onError(res)
            }
        })
        return result
    }

    override fun getUser(): Observable<User> {
        val result = AsyncSubject.create<User>()
        HyperTrack.getUser(object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val res = response.responseObject as User
                result.onNext(res)
                result.onComplete()
            }

            override fun onError(error: ErrorResponse) {
                val res = Throwable(error.errorMessage)
                result.onError(res)
            }
        })
        return result
    }

    override fun getAddressLocation(latLng: String): Observable<MutableList<LocationAddress>> {
        return ApiClient.instance.getAddressLocation(latLng).toObservable().map { it.results }
    }

    override fun getLocationDetail(placeId: String?, key: String): Observable<ResultPlaceDetail> {
        return ApiClient.instance.getLocationDetail(placeId, key).toObservable()
    }

    override fun searchLocations(input: String, key: String, language: String, sensor: Boolean)
            : Observable<AutoCompleteResult> {
        return ApiClient.instance.searchLocations(input, key, language, sensor).toObservable()
    }

    override fun createGroup(name: String): Observable<Group> {
        return HypertrackApi.instance.createGroup(name).toObservable()
    }

    override fun getGroupInfo(groupId: String): Observable<Group> {
        return HypertrackApi.instance.getGroupInfo(groupId).toObservable()
    }

    override fun getGroupMembers(groupId: String): Observable<MutableList<User>> {
        return HypertrackApi.instance.getGroupMembers(groupId).toObservable().map { it.results }
    }

    override fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        return HypertrackApi.instance.addUserToGroup(userId, body).toObservable()
    }

    override fun removeUserFromGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        return HypertrackApi.instance.removeUserFromGroup(userId, body).toObservable()
    }
}
