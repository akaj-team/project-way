package vn.asiantech.way.ui.search

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import vn.asiantech.way.R
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
        return LocationAdapterUI().createView(AnkoContext.Companion.create(parent.context,
                parent, false)).tag as? LocationViewHolder
    }

    /**
     * View holder of RecyclerView's item.
     */
    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgLocationIcon: ImageView
                = itemView.find(R.id.location_adapter_ui_img_location_icon)
        private val tvLocationName: TextView
                = itemView.find(R.id.location_adapter_ui_tv_location_name)
        private val tvLocationAddress: TextView
                = itemView.find(R.id.location_adapter_ui_tv_location_format_address)
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
                imgLocationIcon.setImageResource(locationIcon)
                tvLocationName.text = name
                tvLocationAddress.text = formatAddress
            }
        }
    }

    /**
     * Item layout of RecyclerView.
     */
    inner class LocationAdapterUI : AnkoComponent<ViewGroup> {

        private lateinit var imgLocationIcon: ImageView
        private lateinit var tvLocationName: TextView
        private lateinit var tvLocationAddress: TextView

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val view = with(ui) {
                relativeLayout {
                    lparams(matchParent, wrapContent)
                    backgroundColor = Color.WHITE
                    view {
                        id = R.id.location_adapter_ui_view_break_line
                        backgroundResource = R.color.colorSearchScreenBackground
                    }.lparams(matchParent, dimen(R.dimen.break_line_view_height)) {
                        bottomMargin = dimen(R.dimen.break_line_top_bot_margin)
                        topMargin = dimen(R.dimen.break_line_top_bot_margin)
                        leftMargin = dimen(R.dimen.break_line_left_margin)
                    }

                    imgLocationIcon = imageView {
                        id = R.id.location_adapter_ui_img_location_icon
                    }.lparams {
                        margin = dimen(R.dimen.default_padding_margin)
                        below(R.id.location_adapter_ui_view_break_line)
                    }

                    tvLocationName = textView {
                        id = R.id.location_adapter_ui_tv_location_name
                        singleLine = true
                        textSizeDimen = R.dimen.search_screen_text_size
                    }.lparams {
                        below(R.id.location_adapter_ui_view_break_line)
                        leftMargin = dimen(R.dimen.default_padding_margin)
                        rightOf(R.id.location_adapter_ui_img_location_icon)
                    }

                    tvLocationAddress = textView {
                        id = R.id.location_adapter_ui_tv_location_format_address
                        singleLine = true
                        textSizeDimen = R.dimen.search_screen_text_size
                    }.lparams {
                        below(R.id.location_adapter_ui_tv_location_name)
                        leftMargin = dimen(R.dimen.default_padding_margin)
                        rightOf(R.id.location_adapter_ui_img_location_icon)
                    }
                }
            }
            view.tag = LocationViewHolder(view)
            return view
        }
    }
}
