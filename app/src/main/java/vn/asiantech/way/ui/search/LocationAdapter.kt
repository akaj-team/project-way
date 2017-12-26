package vn.asiantech.way.ui.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.data.model.WayLocation

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 28/11/2017
 */
class LocationAdapter(val locations: MutableList<WayLocation>)
    : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    var onItemClick: (location: WayLocation) -> Unit = {}

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: LocationViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder? {
        val locationItemUI = LocationItemUI()
        return LocationViewHolder(locationItemUI,
                locationItemUI.createView(AnkoContext.Companion.create(parent.context, parent, false)))
    }

    /**
     * View holder of RecyclerView's item.
     */
    inner class LocationViewHolder(val ui: LocationItemUI, itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClick {
                onItemClick(locations[adapterPosition])
            }
        }

        /**
         * Bind data to view holder.
         */
        fun onBind() {
            with(locations[adapterPosition]) {
                ui.imgLocationIcon.setImageResource(locationIcon)
                ui.tvLocationName.text = name
                ui.tvLocationAddress.text = formatAddress
            }
        }
    }
}
