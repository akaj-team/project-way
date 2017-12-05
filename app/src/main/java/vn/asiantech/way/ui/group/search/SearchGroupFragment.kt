package vn.asiantech.way.ui.group.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.google.gson.Gson
import com.hypertrack.lib.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.toast
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.extension.observeOnUiThread
import vn.asiantech.way.ui.base.BaseFragment
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit


/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 04/12/2017.
 */
class SearchGroupFragment : BaseFragment() {

    companion object {

        const val KEY_USER = "key_user"

        /**
         * Get instance of SearchGroupFragemt with a given user.
         */
        fun getInstance(user: User): SearchGroupFragment {
            val instance = SearchGroupFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, user)
            instance.arguments = bundle
            return instance
        }
    }

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private var user: User? = null
    private var groups = mutableListOf<Group>()
    private lateinit var userRef: DatabaseReference
    private var currentRequest: Invite? = null

    private lateinit var adapter: SearchGroupAdapter
    private lateinit var ui: SearchGroupFragmentUI
    private val searchGroupViewModel = SearchGroupViewModel()
    private val searchGroupObservable = PublishSubject.create<String>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ui = SearchGroupFragmentUI()
        user = arguments.getSerializable(KEY_USER) as? User
        userRef = firebaseDatabase.getReference("user/" + user?.id + "/request")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) = Unit

            override fun onDataChange(p0: DataSnapshot?) {
                val gson = Gson()
                currentRequest = gson.fromJson(gson.toJson(p0?.value), Invite::class.java)
            }
        })
        return ui.createView(AnkoContext.create(context, this))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchGroupAdapter(context, groups) {

            if (currentRequest != null) {
                val groupRef = firebaseDatabase.getReference("group/" + currentRequest?.to
                        + "/request/" + user?.id)
                groupRef.removeValue()
            }
            val invite = Invite(user?.id!!, user?.id!!, it.name, true)
            userRef.setValue(invite)
            firebaseDatabase.getReference("group/" + it.id
                    + "/request/" + user?.id).setValue(invite)
            currentRequest = invite
        }
    }

    override fun onBindViewModel() {
        addDisposables(searchGroupObservable
                .observeOnUiThread()
                .debounce(AppConstants.WAITING_TIME_FOR_SEARCH_FUNCTION, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe({
                    searchGroupViewModel
                            .searchGroup(it)
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::updateRecyclerViewGroup,
                                    {
                                        toast(R.string.error_message)
                                    })
                })
        )
    }

    internal fun searchGroups(query: String) {
        searchGroupObservable.onNext(query)
    }

    private fun updateRecyclerViewGroup(data: List<Group>) {
        groups.clear()
        groups.addAll(data)
        adapter.notifyDataSetChanged()
    }

    internal fun onBackClick() {
        //TODO : send broadcast to GroupActivity
    }
}
