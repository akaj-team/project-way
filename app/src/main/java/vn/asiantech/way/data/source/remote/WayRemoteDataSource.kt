package vn.asiantech.way.data.source.remote

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.internal.common.models.VehicleType
import com.hypertrack.lib.models.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.SingleSubject
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.datasource.WayDataSource
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
            override fun onSuccess(response: SuccessResponse) {
                val res = ResponseStatus(true, "Success")
                result.onNext(res)
                result.onComplete()
            }

            override fun onError(error: ErrorResponse) {
                val throwable = Throwable(error.errorMessage)
                result.onError(throwable)
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
                val throwable = Throwable(error.errorMessage)
                result.onError(throwable)
            }
        })
        return result
    }

    override fun getUser(): Single<User> {
        val result = SingleSubject.create<User>()
        HyperTrack.getUser(object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val res = response.responseObject as? User
                res?.let { result.onSuccess(it) }
            }

            override fun onError(error: ErrorResponse) {
                val throwable = Throwable(error.errorMessage)
                result.onError(throwable)
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
        val result = PublishSubject.create<User>()
        HypertrackApi.instance.addUserToGroup(userId, body)
                .toObservable()
                .subscribe {
                    FirebaseDatabase.getInstance().getReference("user/$userId/groupId")
                            .setValue("join")
                    result.onNext(it)
                }
        return result
    }

    override fun removeUserFromGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        return HypertrackApi.instance.removeUserFromGroup(userId, body).toObservable()
    }

    override fun getCurrentLocation(): Observable<HyperTrackLocation> {
        val result = AsyncSubject.create<HyperTrackLocation>()
        HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val res = HyperTrackLocation((response.responseObject) as? Location?)
                result.onNext(res)
                result.onComplete()
            }

            override fun onError(error: ErrorResponse) {
                val throwable = Throwable(error.errorMessage)
                result.onError(throwable)
            }
        })
        return result
    }

    override fun getETA(destination: LatLng, vehicle: VehicleType): Observable<Float> {
        val result = AsyncSubject.create<Float>()
        HyperTrack.getETA(destination, vehicle, object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val res = (response.responseObject as? Double)?.toFloat()
                res?.let { result.onNext(it) }
                result.onComplete()
            }

            override fun onError(error: ErrorResponse) {
                val throwable = Throwable(error.errorMessage)
                result.onError(throwable)
            }
        })
        return result
    }

    override fun createAndAssignAction(builder: ActionParamsBuilder): Observable<Action> {
        val result = AsyncSubject.create<Action>()
        HyperTrack.createAndAssignAction(builder.build(), object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                val res = response.responseObject as? Action
                res?.let { result.onNext(it) }
                result.onComplete()
            }

            override fun onError(error: ErrorResponse) {
                val throwable = Throwable(error.errorMessage)
                result.onError(throwable)
            }
        })
        return result
    }
}
