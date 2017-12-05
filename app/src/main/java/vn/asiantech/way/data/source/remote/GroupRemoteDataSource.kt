package vn.asiantech.way.data.source.remote

import com.google.firebase.database.*
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.datasource.GroupDataSource
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackApi

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 04/12/2017
 */
class GroupRemoteDataSource : GroupDataSource {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun getGroupInfo(groupId: String): Observable<Group> {
        val result = PublishSubject.create<Group>()
        val groupRef = firebaseDatabase.getReference("group/$groupId/info")
        groupRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                result.onError(Throwable(p0?.message))
            }

            override fun onDataChange(p0: DataSnapshot?) {
                result.onNext(Gson().fromJson(p0?.value.toString(), Group::class.java))
            }
        })
        return result
    }

    override fun getGroupId(userId: String): Observable<String> {
        val result = PublishSubject.create<String>()
        val groupRef = firebaseDatabase.getReference("user/$userId/groupId")
        groupRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onDataChange(p0: DataSnapshot?) {
                val refValue = p0?.value.toString()
                if (refValue.isNotEmpty()) {
                    groupRef.setValue("")
                } else {
                    result.onNext(refValue)
                }
            }
        })
        return result
    }

    override fun getInvite(userId: String): Observable<Invite> {
        val result = PublishSubject.create<Invite>()
        val inviteRef = firebaseDatabase.getReference("user/$userId/invites")
        inviteRef.addChildEventListener(object : SimpleChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val invite = Gson().fromJson(p0?.value.toString(), Invite::class.java)
                if (invite != null) {
                    result.onNext(invite)
                }
            }
        })
        return result
    }

    override fun getGroupRequest(groupId: String): Observable<Invite> {
        val result = PublishSubject.create<Invite>()
        val inviteRef = firebaseDatabase.getReference("group/$groupId/request")
        inviteRef.addChildEventListener(object : SimpleChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val invite = Gson().fromJson(p0?.value.toString(), Invite::class.java)
                if (invite != null) {
                    result.onNext(invite)
                }
            }
        })
        return result
    }

    override fun postGroupInfo(group: Group): Observable<Boolean> {
        val result = PublishSubject.create<Boolean>()
        val groupRef = firebaseDatabase.getReference("group/${group.id}/info")
        groupRef.setValue(group) { databaseError, dataSuccess ->
            if (databaseError != null) {
                result.onNext(false)
            }
            if (dataSuccess != null) {
                result.onNext(true)
            }
        }
        return result
    }

    override fun changeGroupOwner(groupId: String, newOwner: String): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        val inviteRef = firebaseDatabase.getReference("group/$groupId/info/ownerId")
        inviteRef.setValue(newOwner) { databaseError, dataSuccess ->
            if (databaseError != null) {
                result.onSuccess(false)
            }
            if (dataSuccess != null) {
                result.onSuccess(true)
            }
        }
        return result
    }

    override fun removeGroup(groupId: String): Observable<Boolean> {
        // TODO: 04/12/2017
        // Will handle when we want.
        TODO("not implemented")
    }

    override fun postInvite(userId: String, invite: Invite): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        val inviteRef = firebaseDatabase.getReference("user/$userId/${invite.to}")
        inviteRef.setValue(invite) { databaseError, dataSuccess ->
            if (databaseError != null) {
                result.onSuccess(false)
            }
            if (dataSuccess != null) {
                result.onSuccess(true)
            }
        }
        return result
    }

    override fun removeUserFromGroup(userId: String): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        HypertrackApi.instance.removeUserFromGroup(userId, BodyAddUserToGroup(null))
                .doOnSuccess { result.onSuccess(true) }
                .doOnError { result.onError(it) }
        return result
    }

    override fun searchGroup(groupName: String): Observable<List<Group>> {
        return HypertrackApi.instance.searchGroup(groupName)
                .toObservable()
                .map { it.groups }
    }

    override fun getCurrentRequestOfUser(userId: String): Observable<Invite> {
        val result = PublishSubject.create<Invite>()
        val requestRef = firebaseDatabase.getReference("user/$userId/request")
        requestRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onDataChange(p0: DataSnapshot?) {
                val gson = Gson()
                val currentRequest = gson.fromJson(gson.toJson(p0?.value), Invite::class.java)
                result.onNext(currentRequest)
            }
        })
        return result
    }

    override fun postRequestToGroup(groupId: String, request: Invite) {
        val requestRef = firebaseDatabase.getReference("group/$groupId/request/${request.to}")
        requestRef.setValue(request)
    }

    override fun postRequestToUser(userId: String, request: Invite) {
        val requestRef = firebaseDatabase.getReference("user/$userId/request")
        requestRef.setValue(request)
    }

    override fun searchUser(name: String): Observable<List<User>> {
        return HypertrackApi.instance.searchUser(name).toObservable().map { it.users }
    }

    /**
     * This interface used to make ChildEventListener become a simple interface.
     */
    interface SimpleChildEventListener : ChildEventListener {

        override fun onChildAdded(p0: DataSnapshot?, p1: String?) = Unit

        override fun onChildMoved(p0: DataSnapshot?, p1: String?) = Unit

        override fun onCancelled(p0: DatabaseError?) = Unit

        override fun onChildChanged(p0: DataSnapshot?, p1: String?) = Unit

        override fun onChildRemoved(p0: DataSnapshot?) = Unit
    }
}
