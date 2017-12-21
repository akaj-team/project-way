package vn.asiantech.way.data.source.remote

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.hypertrack.lib.HyperTrack
import com.hypertrack.lib.callbacks.HyperTrackCallback
import com.hypertrack.lib.internal.common.models.VehicleType
import com.hypertrack.lib.models.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.datasource.WayDataSource
import vn.asiantech.way.data.source.remote.googleapi.ApiClient
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackApi
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.utils.LocationUtil
import java.io.IOException
import java.util.*

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

    override fun getAddressLocation(latLng: String): Observable<MutableList<LocationAddress>> = ApiClient.instance.getAddressLocation(latLng).toObservable().map { it.results }

    override fun getLocationDetail(placeId: String?): Observable<ResultPlaceDetail> = ApiClient.instance.getLocationDetail(placeId).toObservable()

    override fun searchLocations(input: String, language: String, sensor: Boolean)
            : Observable<AutoCompleteResult> = ApiClient.instance.searchLocations(input, language, sensor).toObservable()

    override fun getLocationDistance(origin: String, destination: String): Observable<ResultDistance> =
            ApiClient.instance.getLocationDistance(origin, destination).toObservable()

    override fun getListLocation(url: String): Observable<ResultRoad> =
            ApiClient.instance.getListLocation(url)

    override fun createGroup(name: String): Observable<Group> = HypertrackApi.instance.createGroup(name).toObservable()

    override fun getGroupInfo(groupId: String): Observable<Group> = HypertrackApi.instance.getGroupInfo(groupId).toObservable()

    override fun getGroupMembers(groupId: String): Observable<MutableList<User>> = HypertrackApi.instance.getGroupMembers(groupId).toObservable().map { it.results }

    override fun addUserToGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        val result = PublishSubject.create<User>()
        val userGroupRef = FirebaseDatabase.getInstance().getReference("user/$userId/groupId")
        userGroupRef.setValue(userId)
                .addOnFailureListener {
                    result.onError(it)
                }
                .addOnSuccessListener {
                    HypertrackApi.instance.addUserToGroup(userId, body).toObservable()
                            .subscribeOn(Schedulers.io())
                            .subscribe({
                                result.onNext(it)
                            }, {
                                result.onError(it)
                            })
                }
        return result
    }

    override fun removeUserFromGroup(userId: String, body: BodyAddUserToGroup): Observable<User> {
        val result = PublishSubject.create<User>()
        val userGroupRef = FirebaseDatabase.getInstance().getReference("user/$userId/groupId")
        userGroupRef.setValue(userId)
                .addOnFailureListener {
                    result.onError(it)
                }
                .addOnSuccessListener {
                    HypertrackApi.instance.removeUserFromGroup(userId, body).toObservable()
                            .subscribeOn(Schedulers.io())
                            .subscribe({
                                result.onNext(it)
                            }, {
                                result.onError(it)
                            })
                }
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

    override fun getTrackingURL(): Single<String> {
        val link = SingleSubject.create<String>()
        val builder = ActionParamsBuilder()
        HyperTrack.createAndAssignAction(builder.build(), object : HyperTrackCallback() {
            override fun onSuccess(response: SuccessResponse) {
                if (response.responseObject != null) {
                    val action = response.responseObject as? Action
                    HyperTrack.clearServiceNotificationParams()
                    val url = action?.trackingURL
                    url?.let { link.onSuccess(it) }
                }
            }

            override fun onError(errorResponse: ErrorResponse) {
                val throwable = Throwable(errorResponse.errorMessage)
                link.onError(throwable)
            }
        })
        return link
    }

    override fun getLocationName(context: Context, latLng: LatLng): Single<String> {
        val locationName = SingleSubject.create<String>()
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            locationName.onSuccess(address.getAddressLine(0))
        } else {
            locationName.onSuccess("")
        }
        return locationName
    }

    override fun getCurrentLocationHyperTrack(): Single<HyperTrackLocation> {
        val result = SingleSubject.create<HyperTrackLocation>()
        HyperTrack.getCurrentLocation(object : HyperTrackCallback() {
            override fun onSuccess(p0: SuccessResponse) {
                try {
                    val hyperTrackLocation = HyperTrackLocation((p0.responseObject) as Location?)
                    result.onSuccess(hyperTrackLocation)
                } catch (e: IOException) {
                    Log.d("zxc", "ErrorResponse: " + e.message)
                }
            }

            override fun onError(errorResponse: ErrorResponse) {
                val throwable = Throwable(errorResponse.errorMessage)
                result.onError(throwable)
            }
        })
        return result
    }

    override fun getCurrentLocation(context: Context): Single<Location> {
        val result = SingleSubject.create<Location>()
        LocationUtil(context).getCurrentLocation()?.let {
            result.onSuccess(it)
        }
        return result
    }
}
