package vn.asiantech.way.ui.group.request

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.item_request.view.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 20/11/2017
 */
class RequestListAdapter(val users: MutableList<User>, val listener: OnViewItemClick)
    : RecyclerView.Adapter<RequestListAdapter.RequestViewHolder>() {
    override fun onBindViewHolder(holder: RequestViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_request, parent, false))
    }

    override fun getItemCount() = users.size

    /**
     * Custom item view requests list.
     */
    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.tvOk.setOnClickListener {
                listener.onOkClick(users[adapterPosition].id)
            }
            itemView.tvCancel.setOnClickListener {
                listener.onCancelClick(users[adapterPosition].id)
            }
        }

        /**
         * Bind data to view holder
         */
        fun onBind() {
            itemView.tvName.text = users[adapterPosition].name
        }
    }

    /**
     * This interface used to handle view onclick event of item holder.
     */
    interface OnViewItemClick {

        /**
         * Handle onclick of ok text view.
         */
        fun onOkClick(userId: String)

        /**
         * Handle onclick of cancel text view
         */
        fun onCancelClick(userId: String)
    }
}
