package vn.asiantech.way.ui.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import at.blogc.android.views.ExpandableTextView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
import vn.asiantech.way.data.model.TrackingInformation

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 27/11/2017.
 */
class HomeAdapter(private val locations: List<TrackingInformation>,
                  val onClickItem: (Int) -> Unit)
    : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder? {
        val ui = HomeAdapterUI()
        return HomeViewHolder(ui, ui.createView(AnkoContext.Companion.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        holder?.bindHomeViewHolder(locations[position])
    }

    override fun getItemCount(): Int = locations.size

    /**
     * To save data for items in recyclerView of locations
     */
    inner class HomeViewHolder(val ui: HomeAdapterUI, val item: View) : RecyclerView.ViewHolder(item) {

        init {
            ui.llItemLocation.onClick {
                onClickItem(adapterPosition)
                ui.expTvDescription.toggle()
            }

//            ui.imgArrowDown.onClick {
//            }

            ui.expTvDescription.onExpandListener = object : ExpandableTextView.OnExpandListener {
                override fun onExpand(view: ExpandableTextView) {
                    ui.imgArrowDown.imageResource = R.drawable.ic_keyboard_arrow_down_black_18dp
                }

                override fun onCollapse(view: ExpandableTextView) {
                    ui.imgArrowDown.imageResource = R.drawable.ic_keyboard_arrow_right_black_18dp
                }
            }
        }

        internal fun bindHomeViewHolder(location: TrackingInformation) {
            with(location) {
                ui.tvTime.text = time
                ui.tvStatus.text = status
                ui.expTvDescription.text = description
                ui.expTvDescription.post {
                    if (location.isChoose) {
                        if (ui.expTvDescription.lineCount > 1) {
                            ui.imgArrowDown.visibility = View.VISIBLE
                            ui.expTvDescription.toggle()
                        } else {
                            ui.imgArrowDown.visibility = View.GONE
                        }
                        item.backgroundResource = R.drawable.bg_item_location_choose
                        ui.imgPoint.backgroundResource = R.drawable.ic_point_white
                    } else {
                        item.backgroundResource = R.drawable.bg_item_location_default
                        ui.imgPoint.backgroundResource = R.drawable.ic_point_pink
                        ui.imgArrowDown.visibility = View.GONE
                    }
                }
            }
        }
    }
}
