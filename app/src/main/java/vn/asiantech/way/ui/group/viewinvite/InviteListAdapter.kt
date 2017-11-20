package vn.asiantech.way.ui.group.viewinvite

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_invite.view.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.Invite

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 17/11/2017
 */
class InviteListAdapter(val invites: MutableList<Invite>, val listener: OnItemClick)
    : RecyclerView.Adapter<InviteListAdapter.InviteViewHolder>() {
    override fun onBindViewHolder(holder: InviteViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteViewHolder {
        return InviteViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_invite, parent, false))
    }

    override fun getItemCount() = invites.size

    /**
     * Custom item of invites list.
     */
    inner class InviteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.tvOk.setOnClickListener {
                listener.onOkClick(invites[adapterPosition])
                invites.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
            itemView.tvCancel.setOnClickListener {
                listener.onCancelClick(invites[adapterPosition])
                invites.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

        /**
         * Bind data to InviteViewHolder.
         */
        fun onBind() {
            itemView.tvName.text = invites[adapterPosition].groupName
        }
    }

    /**
     * This interface used to handle item of InviteViewHolder click.
     */
    interface OnItemClick {

        /**
         * This method used to handle button ok on click.
         */
        fun onOkClick(invite: Invite)

        /**
         * This method used to handle button cancel on click.
         */
        fun onCancelClick(invite: Invite)
    }
}
