package vn.asiantech.way.ui.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_recyclerview_location.view.*
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 27/09/2017.
 */
class HomeAdapter(private val locations: List<Location>, val onClickItem: (Int) -> Unit) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val itemView = inflater.inflate(R.layout.item_recyclerview_location, parent, false)
        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        val homeViewHolder = holder as HomeViewHolder
        homeViewHolder.bindHomeViewHolder(locations[position])
        if (locations[position].isChoose == true) {
            homeViewHolder.itemView.setBackgroundResource(R.drawable.bg_item_location_choose)
            homeViewHolder.itemView.imgPoint.setBackgroundResource(R.drawable.ic_point_white)
        } else {
            homeViewHolder.itemView.setBackgroundResource(R.drawable.bg_item_location_default)
            homeViewHolder.itemView.imgPoint.setBackgroundResource(R.drawable.ic_point_pink)
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindHomeViewHolder(location: Location) {
            with(location) {
                itemView.tvTime.text = time
                itemView.tvStatus.text = status
                itemView.tvDescription.text = description
            }

            itemView.llItemLocation.setOnClickListener {
                onClickItem(layoutPosition)
            }
        }
    }
}
