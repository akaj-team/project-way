package vn.asiantech.way.data.source.remote

import com.google.firebase.database.*
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.datasource.GroupDataSource

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
            override fun onCancelled(p0: DatabaseError?) {
                result.onError(Throwable(p0?.message))
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val id = p0?.getValue(String::class.java)
                if (id != null) {
                    result.onNext(id)
                }
            }
        })
        return result
    }

    /**
     * This method used to get invite list of a given user.
     *
     * @param userId - id of given user.
     */
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

    override fun getRequest(groupId: String): Observable<Invite> {
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

    override fun upGroupInfo(group: Group): Observable<Boolean> {
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

    override fun changeOwner(groupId: String, newOwner: String): Observable<Boolean> {
        TODO("not implemented")
    }

    override fun removeGroup(groupId: String): Observable<Boolean> {
        // Will handle when we want.
        TODO("not implemented")
    }

    override fun upInvite(userId: String, invite: Invite): Single<Boolean> {
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
        TODO("not implemented")
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
