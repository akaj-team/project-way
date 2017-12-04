package vn.asiantech.way.data.source.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.model.Group
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
}
