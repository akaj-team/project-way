package vn.asiantech.way.ui.group

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.item_user_list.view.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 17/11/2017
 */
class UserListAdapter(val users: MutableList<User>, val listener: UserListAdapter.OnItemOnclick)
    : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_list, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = users.size

    /**
     * This class used to custom item of user list.
     */
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.tvInvite.setOnClickListener {
                listener.onInviteOnclick(users[adapterPosition].id)
                itemView.tvInvite.visibility = View.GONE
            }
        }

        /**
         * Bind data to UserViewHolder
         */
        fun onBind() {
            with(users[adapterPosition]) {
                itemView.tvName.text = name
                Glide.with(itemView.context).load(photo).into(itemView.imgAvatar)
                if (groupId != null) {
                    itemView.tvInvite.visibility = View.GONE
                } else {
                    itemView.tvInvite.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * This interface used to handle item on click of UserViewHolder
     */
    interface OnItemOnclick {

        /**
         * This method used to handle onclick of invite button
         */
        fun onInviteOnclick(user: String)
    }
}
