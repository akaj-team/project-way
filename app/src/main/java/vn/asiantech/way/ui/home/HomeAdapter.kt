package vn.asiantech.way.ui.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import at.blogc.android.views.ExpandableTextView
import kotlinx.android.synthetic.main.item_recyclerview_location.view.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.Location
import vn.asiantech.way.extension.inflate

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by atHangTran on 27/09/2017.
 */
class HomeAdapter(private val locations: List<Location>, val onClickItem: (Int) -> Unit)
    : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeViewHolder {
        val inflatedView = parent!!.inflate(R.layout.item_recyclerview_location, false)
        return HomeViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        holder?.bindHomeViewHolder(locations[position])
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    /**
     * To save data for items in recyclerView of locations
     */
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun bindHomeViewHolder(location: Location) {
            with(location) {
                itemView.tvTime.text = time
                itemView.tvStatus.text = status
                itemView.expTvDescription.text = description
                itemView.expTvDescription.post {
                    if (location.isChoose) {
                        if (itemView.expTvDescription.lineCount > 1) {
                            itemView.imgArrow.visibility = View.VISIBLE
                        } else {
                            itemView.imgArrow.visibility = View.GONE
                        }
                        itemView.setBackgroundResource(R.drawable.bg_item_location_choose)
                        itemView.imgPoint.setBackgroundResource(R.drawable.ic_point_white)
                    } else {
                        itemView.setBackgroundResource(R.drawable.bg_item_location_default)
                        itemView.imgPoint.setBackgroundResource(R.drawable.ic_point_pink)
                        itemView.imgArrow.visibility = View.GONE
                    }
                }
            }

            itemView.llItemLocation.setOnClickListener {
                onClickItem(adapterPosition)
            }

            itemView.imgArrow.setOnClickListener {
                itemView.expTvDescription.toggle()
            }

            itemView.expTvDescription.onExpandListener = object : ExpandableTextView.OnExpandListener {
                override fun onExpand(view: ExpandableTextView) {
                    itemView.imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
                }

                override fun onCollapse(view: ExpandableTextView) {
                    itemView.imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
                }
            }
        }
    }
}
