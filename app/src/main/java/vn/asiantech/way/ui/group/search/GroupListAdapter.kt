package vn.asiantech.way.ui.group.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_search_group.view.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.group.Group

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 16/11/2017
 */
class GroupListAdapter(val groups: MutableList<Group>, val listener: GroupItemOnclick)
    : RecyclerView.Adapter<GroupListAdapter.GroupHolder>() {
    override fun onBindViewHolder(holder: GroupHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = groups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        return GroupHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_group, parent, false))
    }

    /**
     * Custom view item  of groups list.
     */
    inner class GroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.tvJoinGroup.setOnClickListener {
                listener.onJoinGroupClick(groups[adapterPosition])
                itemView.tvJoinGroup.visibility = View.INVISIBLE
            }
        }

        /**
         * Bind data to item view.
         */
        fun onBind() {
            with(groups[adapterPosition]) {
                itemView.tvName.text = name
            }
        }
    }

    /**
     * This interface used to handle item on click of GroupHolder.
     */
    interface GroupItemOnclick {

        /**
         * This function used to handle onclick of join group button.
         */
        fun onJoinGroupClick(group: Group)
    }
}
