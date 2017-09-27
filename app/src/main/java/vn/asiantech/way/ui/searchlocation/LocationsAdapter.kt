package vn.asiantech.way.ui.searchlocation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_search_location.view.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov. on 25/09/2017.
 */

class LocationsAdapter(var mMyLocations: MutableList<MyLocation>, val mListener: RecyclerViewOnItemClickListener) : RecyclerView.Adapter<LocationsAdapter.LocationHolder>() {

    override fun onBindViewHolder(holder: LocationHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_search_location, parent, false)
        return LocationHolder(view)
    }

    override fun getItemCount() = mMyLocations.size


    inner class LocationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                mListener.onItemClick(mMyLocations[adapterPosition])
            }
        }

        fun onBind() {
            itemView.tvLocationName.text = mMyLocations[adapterPosition].name
            itemView.tvFormatAddress.text = mMyLocations[adapterPosition].formatted_address
        }
    }

    interface RecyclerViewOnItemClickListener {
        fun onItemClick(myLocation: MyLocation)
    }
}
