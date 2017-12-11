package vn.asiantech.way.ui.group.info

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hypertrack.lib.models.User
import kotlinx.android.synthetic.main.item_group_member.view.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 10/11/2017
 */
class MemberListAdapter(val userId: String, val members: MutableList<User>)
    : RecyclerView.Adapter<MemberListAdapter.MemberViewHolder>() {
    override fun getItemCount() = members.size

    override fun onBindViewHolder(holder: MemberViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_group_member, parent, false))
    }

    /**
     * This class used to custom item of members list.
     */
    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.imgCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(itemView.context.getString(R.string.call_intent,
                        members[adapterPosition].lookupId))
                itemView.context.startActivity(intent)
            }
        }

        /**
         * Binding data to view.
         */
        fun onBind() {
            with(members[adapterPosition]) {
                if (userId != id && lookupId != null) {
                    itemView.imgCall.visibility = View.VISIBLE
                } else {
                    itemView.imgCall.visibility = View.GONE
                }
                itemView.tvName.text = name
                Glide.with(itemView.context).load(photo).into(itemView.imgAvatar)
            }
            if (adapterPosition % 2 == 0) {
                itemView.setBackgroundColor(Color.WHITE)
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.colorCyan))
            }
        }
    }
}
