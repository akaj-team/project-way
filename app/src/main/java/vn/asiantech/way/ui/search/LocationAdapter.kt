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

    companion object {
        internal const val ID_VIEW_BREAK_LINE = 1001
        internal const val ID_IMG_LOCATION_ICON = 1002
        internal const val ID_TV_LOCATION_NAME = 1003
        internal const val ID_TV_LOCATION_FORMAT_ADDRESS = 1004
    }

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
    inner class LocationViewHolder(itemView: View,
                                   private val imgLocationIcon: ImageView,
                                   private val tvLocationName: TextView,
                                   private val tvLocationAddress: TextView)
        : RecyclerView.ViewHolder(itemView) {

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
                if (isHistory != null && isHistory == true) {
                    imgLocationIcon.setImageResource(R.drawable.ic_access_time)
                } else {
                    imgLocationIcon.setImageResource(R.drawable.ic_marker_gray)
                }
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
                        id = ID_VIEW_BREAK_LINE
                        backgroundResource = R.color.colorSearchScreenBackground
                    }.lparams(matchParent, dimen(R.dimen.break_line_view_height)) {
                        bottomMargin = dimen(R.dimen.break_line_top_bot_margin)
                        topMargin = dimen(R.dimen.break_line_top_bot_margin)
                        leftMargin = dimen(R.dimen.break_line_left_margin)
                    }

                    imgLocationIcon = imageView {
                        id = ID_IMG_LOCATION_ICON
                    }.lparams {
                        margin = dimen(R.dimen.default_padding_margin)
                        below(ID_VIEW_BREAK_LINE)
                    }

                    tvLocationName = textView {
                        id = ID_TV_LOCATION_NAME
                        singleLine = true
                        textSizeDimen = R.dimen.search_screen_text_size
                    }.lparams {
                        below(ID_VIEW_BREAK_LINE)
                        leftMargin = dimen(R.dimen.default_padding_margin)
                        rightOf(ID_IMG_LOCATION_ICON)
                    }

                    tvLocationAddress = textView {
                        id = ID_TV_LOCATION_FORMAT_ADDRESS
                        singleLine = true
                        textSizeDimen = R.dimen.search_screen_text_size
                    }.lparams {
                        below(ID_TV_LOCATION_NAME)
                        leftMargin = dimen(R.dimen.default_padding_margin)
                        rightOf(ID_IMG_LOCATION_ICON)
                    }
                }
            }
            view.tag = LocationViewHolder(view, imgLocationIcon, tvLocationName, tvLocationAddress)
            return view
        }
    }
}
