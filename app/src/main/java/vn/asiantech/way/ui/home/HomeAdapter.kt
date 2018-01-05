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
class HomeAdapter(
        private val locations: List<TrackingInformation>)
    : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    internal var onClickItem: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val ui = HomeAdapterUI()
        return HomeViewHolder(ui, ui.createView(AnkoContext.create(parent.context, parent, false)))
    }

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        holder?.bindHomeViewHolder()
    }

    override fun getItemCount(): Int = locations.size

    /**
     * To save data for items in recyclerView of locations
     */
    inner class HomeViewHolder(val ui: HomeAdapterUI, val item: View) : RecyclerView.ViewHolder(item) {

        init {
            ui.llItemLocation.onClick {
                onClickItem(adapterPosition)
            }

            ui.imgArrowDown.onClick {
                ui.expTvDescription.toggle()
            }

            ui.expTvDescription.onExpandListener = object : ExpandableTextView.OnExpandListener {
                override fun onExpand(view: ExpandableTextView) {
                    ui.imgArrowDown.imageResource = R.drawable.ic_keyboard_arrow_down_black_18dp
                }

                override fun onCollapse(view: ExpandableTextView) {
                    ui.imgArrowDown.imageResource = R.drawable.ic_keyboard_arrow_right_black_18dp
                }
            }
        }

        internal fun bindHomeViewHolder() {
            with(locations[adapterPosition]) {
                ui.run {
                    tvTime.text = time
                    tvStatus.text = status
                    expTvDescription.text = description
                    expTvDescription.post {
                        if (isChoose) {
                            if (expTvDescription.lineCount > 1) {
                                imgArrowDown.visibility = View.VISIBLE
                            } else {
                                imgArrowDown.visibility = View.GONE
                            }
                            item.backgroundResource = R.drawable.bg_item_location_choose
                            imgPoint.backgroundResource = R.drawable.ic_point_white
                        } else {
                            item.backgroundResource = R.drawable.bg_item_location_default
                            imgPoint.backgroundResource = R.drawable.ic_point_pink
                            imgArrowDown.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}
