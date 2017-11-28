package vn.asiantech.way.ui.search

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.*
import vn.asiantech.way.R
import vn.asiantech.way.data.model.MyLocation

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 28/11/2017
 */
class LocationAdapter(val locations: MutableList<MyLocation>, val listener: OnItemClick)
    : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    companion object {
        internal const val ID_VIEW_BREAK_LINE = 2202
        internal const val ID_IMG_LOCATION_ICON = 2203
        internal const val ID_TV_LOCATION_NAME = 2204
        internal const val ID_TV_LOCATION_FORMAT_ADDRESS = 2205

        private const val BREAK_LINE_VIEW_HEIGHT = 0.5f
        private const val BREAK_LINE_LEFT_MARGIN = 64
        private const val BREAK_LINE_TOP_BOT_MARGIN = 2
        private const val DEFAULT_PADDING_MARGIN = 10
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: LocationViewHolder?, position: Int) {
        holder?.onBind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder? {
            return LocationAdapterUI().createView(AnkoContext.Companion.create(parent.context, parent,
                    false)).tag as? LocationViewHolder
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
            itemView.setOnClickListener {
                listener.onItemClick(locations[adapterPosition])
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
                    }.lparams {
                        width = matchParent
                        height = dip(BREAK_LINE_VIEW_HEIGHT)
                        bottomMargin = dip(BREAK_LINE_TOP_BOT_MARGIN)
                        topMargin = dip(BREAK_LINE_TOP_BOT_MARGIN)
                        leftMargin = dip(BREAK_LINE_LEFT_MARGIN)
                    }

                    imgLocationIcon = imageView {
                        id = ID_IMG_LOCATION_ICON
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        margin = dip(DEFAULT_PADDING_MARGIN)
                        below(ID_VIEW_BREAK_LINE)
                    }

                    tvLocationName = textView {
                        id = ID_TV_LOCATION_NAME
                        singleLine = true
                        textSizeDimen = R.dimen.search_screen_text_size
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        below(ID_VIEW_BREAK_LINE)
                        leftMargin = dip(DEFAULT_PADDING_MARGIN)
                        rightOf(ID_IMG_LOCATION_ICON)
                    }

                    tvLocationAddress = textView {
                        id = ID_TV_LOCATION_FORMAT_ADDRESS
                        singleLine = true
                        textSizeDimen = R.dimen.search_screen_text_size
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        below(ID_TV_LOCATION_NAME)
                        leftMargin = dip(DEFAULT_PADDING_MARGIN)
                        rightOf(ID_IMG_LOCATION_ICON)
                    }
                }
            }
            view.tag = LocationViewHolder(view, imgLocationIcon, tvLocationName, tvLocationAddress)
            return view
        }
    }

    /**
     * Listener for item onclick of RecyclerView.
     */
    interface OnItemClick {
        /**
         *  Event on item click.
         */
        fun onItemClick(location: MyLocation)
    }
}
