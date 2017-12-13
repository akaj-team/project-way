package vn.asiantech.way.data.source.remote

import com.google.firebase.database.*
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import org.reactivestreams.Subscriber
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.data.source.datasource.GroupDataSource
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackApi


/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 04/12/2017
 */
class GroupRemoteDataSource : GroupDataSource {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val wayRepository = WayRepository()

    override fun getGroupInfo(groupId: String): Observable<Group> {
        val result = PublishSubject.create<Group>()
        val groupRef = firebaseDatabase.getReference("group/$groupId/info")
        groupRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                result.onError(Throwable(p0?.message))
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0?.value == null) {
                    result.onError(Throwable())
                } else {
                    result.onNext(Gson().fromJson(Gson().toJson(p0.value), Group::class.java))
                }
            }
        })
        return result
    }

    override fun listenerForGroupChange(userId: String): Observable<String> {
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

    override fun postGroupInfo(group: Group): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        val groupRef = firebaseDatabase.getReference("group/${group.id}/info")
        groupRef.setValue(group) { databaseError, dataSuccess ->
            if (databaseError != null) {
                result.onError(databaseError.toException())
            }
            if (dataSuccess != null) {
                result.onSuccess(true)
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

    override fun removeUserFromGroup(userId: String): Single<User> {
        return HypertrackApi.instance.removeUserFromGroup(userId, BodyAddUserToGroup(null))
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
                if (p0?.value == null) {
                    result.onNext(Invite("", "", "", false))
                } else {
                    val gson = Gson()
                    val currentRequest = gson.fromJson(gson.toJson(p0.value), Invite::class.java)
                    result.onNext(currentRequest)
                }
            }
        })
        return result
    }

    override fun postRequestToGroup(groupId: String, request: Invite): Single<Boolean> {
        val gson = Gson()
        val result = SingleSubject.create<Boolean>()
        val userRequest = firebaseDatabase.getReference("user/${request.from}/request")
        userRequest.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                result.onError(Throwable(p0?.message))
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0?.value == null) {
                    userRequest.setValue(request)
                    val requestRef = firebaseDatabase.getReference("group/$groupId" +
                            "/request/${request.from}")
                    requestRef.setValue(request).addOnSuccessListener {
                        result.onSuccess(true)
                    }.addOnFailureListener {
                        result.onError(it)
                    }
                } else {
                    val currentRequest = gson.fromJson(gson.toJson(p0.value), Invite::class.java)
                    val currentGroupRequestRef = firebaseDatabase
                            .getReference("group/${currentRequest.to}/request/${request.from}")
                    currentGroupRequestRef.removeValue()
                            .addOnSuccessListener {
                                userRequest.setValue(request)
                                val requestRef = firebaseDatabase.getReference("group/$groupId" +
                                        "/request/${request.from}")
                                requestRef.setValue(request).addOnSuccessListener {
                                    result.onSuccess(true)
                                }.addOnFailureListener {
                                    result.onError(it)
                                }
                            }
                            .addOnFailureListener {
                                result.onError(it)
                            }
                }
            }
        })
        return result
    }

    override fun postRequestToUser(userId: String, request: Invite): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        val requestRef = firebaseDatabase.getReference("user/$userId/request")
        requestRef.setValue(request).addOnSuccessListener {
            result.onSuccess(true)
        }.addOnFailureListener {
            result.onError(it)
        }
        return result
    }

    override fun searchUser(name: String): Observable<List<User>> {
        return HypertrackApi.instance.searchUser(name).toObservable().map { it.users }
    }

    override fun deleteUserInvite(userId: String, invite: Invite): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        val inviteRef = firebaseDatabase.getReference("user/$userId/invites/${invite.to}")
        inviteRef.removeValue().addOnCompleteListener {
            result.onSuccess(true)
        }.addOnFailureListener {
            result.onError(it)
        }
        return result
    }

    override fun deleteGroupRequest(groupId: String, request: Invite): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        val userRequestRef = firebaseDatabase.getReference("user/${request.to}/request")
        userRequestRef.removeValue()
        val groupRequestRef = firebaseDatabase.getReference("group/$groupId/request/${request.to}")
        groupRequestRef.removeValue().addOnCompleteListener {
            result.onSuccess(true)
        }.addOnFailureListener {
            result.onError(it)
        }
        return result
    }

    override fun deleteCurrentRequestOfUserFromGroup(userId: String, request: Invite): Single<Boolean> {
        TODO("Init later")
    }

    override fun createGroup(groupName: String, ownerId: String): Single<Boolean> {
        return HypertrackApi.instance.createGroup(groupName)
                .flatMap { group ->
                    group.ownerId = ownerId
                    HypertrackApi.instance.addUserToGroup(ownerId, BodyAddUserToGroup(group.id))
                            .flatMap {
                                postGroupInfo(group)
                            }
                }
    }

    override fun getMemberList(groupId: String): Single<MutableList<User>> {
        return HypertrackApi.instance.getGroupMembers(groupId).map { it.results }
    }

    override fun acceptInvite(userId: String, invite: Invite): Single<Boolean> {
        val result = SingleSubject.create<Boolean>()
        val inviteRef = firebaseDatabase.getReference("user/$userId/invites/${invite.to}")
        val userRequest = firebaseDatabase.getReference("user/$userId/request/to")
        inviteRef.removeValue().addOnSuccessListener {
            userRequest.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    result.onError(Throwable(p0?.message))
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0?.value == null) {
                        acceptInviteWhenUserDoNotHaveRequestAtTime(result, userId, invite)
                    } else {
                        acceptInviteWhenUserHaveAnotherRequestAtTime(result, userId, invite, p0.value.toString())
                    }
                }
            })
        }.addOnFailureListener {
            result.onError(it)
        }
        return result
    }

    override fun getUserInfo(groupId: String): Observable<User> {
        val result = PublishSubject.create<User>()
        val inviteRef = firebaseDatabase.getReference("group/$groupId/request")
        inviteRef.addChildEventListener(object : SimpleChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val invite = Gson().fromJson(p0?.value.toString(), Invite::class.java)
                if (invite != null) {
                    HypertrackApi.instance
                            .getUserInfo(invite.to)
                            .subscribe({
                                result.onNext(it)
                            }, {
                                result.onError(it)
                            })
                }
            }
        })
        return result
    }

    private fun acceptInviteWhenUserDoNotHaveRequestAtTime(result: SingleSubject<Boolean>, userId: String, invite: Invite) {
        val userRequest = firebaseDatabase.getReference("user/$userId/request")
        if (invite.request) {
            // This case hanlde when invite sent by group owner.
            wayRepository.addUserToGroup(userId, BodyAddUserToGroup(invite.to))
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        result.onSuccess(true)
                    }, {
                        result.onError(it)
                    })
        } else {
            // This case hanlde when invite sent by group member.
            // We will create a request to group and set value for current request of user.
            userRequest.setValue(invite)
            invite.request = true
            val requestRef = firebaseDatabase.getReference("group/${invite.to}" +
                    "/request/${invite.to}")
            invite.to = userId
            requestRef.setValue(invite).addOnSuccessListener {
                result.onSuccess(true)
            }.addOnFailureListener {
                result.onError(it)
            }
        }
    }

    private fun acceptInviteWhenUserHaveAnotherRequestAtTime(result: SingleSubject<Boolean>, userId: String, invite: Invite,
                                                             currentGroupRequestId: String) {
        val userRequest = firebaseDatabase.getReference("user/$userId/request")
        val currentGroupRequestRef = firebaseDatabase.getReference("group/$currentGroupRequestId/request/$userId")
        currentGroupRequestRef.removeValue().addOnSuccessListener {
            if (invite.request) {
                // This case handle when invite sent by group owner.
                wayRepository.addUserToGroup(userId, BodyAddUserToGroup(invite.to))
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            result.onSuccess(true)
                        }, {
                            result.onError(it)
                        })
                // Delete current request of user.
                userRequest.removeValue()
            } else {
                // This case hanlde when invite sent by group member.
                // We will create a request to group and set value for current request of user.
                invite.request = true
                userRequest.setValue(invite)
                        .addOnSuccessListener {
                            val newGroupRef = firebaseDatabase.getReference("group/${invite.to}/request/$userId")
                            invite.to = userId
                            newGroupRef.setValue(invite)
                                    .addOnSuccessListener {
                                        result.onSuccess(true)
                                    }
                                    .addOnFailureListener {
                                        result.onError(it)
                                    }
                        }
                        .addOnFailureListener {
                            result.onError(it)
                        }
            }
        }.addOnFailureListener {
            result.onError(it)
        }
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

private fun <T> Observable<T>.subscribe(subscriber: Subscriber<T>) {}
