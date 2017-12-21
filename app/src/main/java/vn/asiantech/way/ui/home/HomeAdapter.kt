package vn.asiantech.way.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import at.blogc.android.views.ExpandableTextView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HomeAdapterUI()
            .createView(AnkoContext.create(parent.context, parent, false))
            .tag as? HomeViewHolder

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        holder?.bindHomeViewHolder(locations[position])
    }

    override fun getItemCount(): Int = locations.size
    /**
     * To save data for items in recyclerView of locations
     */
    inner class HomeViewHolder(val item: View) : RecyclerView.ViewHolder(item) {

        private val tvTime: TextView = item.find(R.id.home_adapter_tv_time)
        private val tvStatus: TextView = item.find(R.id.home_adapter_tv_status)
        private val imgArrowDown: ImageView = item.find(R.id.home_adapter_img_arrow_down)
        private val expTvDescription: ExpandableTextView = item.find(R.id.home_adapter_tv_expandable_description)
        private val imgPoint: ImageView = item.find(R.id.home_adapter_img_point)
        private val llItemLocation: LinearLayout = item.find(R.id.home_adapter_ll_location)

        init {
            llItemLocation.onClick {
                onClickItem(adapterPosition)
            }

            imgArrowDown.onClick {
                expTvDescription.toggle()
            }

            expTvDescription.onExpandListener = object : ExpandableTextView.OnExpandListener {
                override fun onExpand(view: ExpandableTextView) {
                    imgArrowDown.imageResource = R.drawable.ic_keyboard_arrow_right_black_18dp
                }

                override fun onCollapse(view: ExpandableTextView) {
                    imgArrowDown.imageResource = R.drawable.ic_keyboard_arrow_down_black_18dp
                }
            }
        }

        internal fun bindHomeViewHolder(location: TrackingInformation) {
            with(location) {
                tvTime.text = time
                tvStatus.text = status
                expTvDescription.text = description
                expTvDescription.post {
                    if (location.isChoose) {
                        if (expTvDescription.lineCount > 1) {
                            imgArrowDown.visibility = View.VISIBLE
                        } else {
                            imgArrowDown.visibility = View.GONE
                        }
                        itemView.backgroundResource = R.drawable.bg_item_location_choose
                        imgPoint.backgroundResource = R.drawable.ic_point_white
                    } else {
                        itemView.backgroundResource = R.drawable.bg_item_location_default
                        imgPoint.backgroundResource = R.drawable.ic_point_pink
                        imgArrowDown.visibility = View.GONE
                    }
                }
            }
        }
    }

    /**
     * Class to bind ViewHolder with Anko layout
     */
    inner class HomeAdapterUI : AnkoComponent<ViewGroup> {

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = ui.apply {
                linearLayout {
                    id = R.id.home_adapter_ll_location
                    gravity = Gravity.CENTER_VERTICAL
                    lparams(matchParent, wrapContent) {
                        verticalMargin = dimen(R.dimen.home_screen_linearLayout_margin)
                        padding = dimen(R.dimen.home_screen_linearLayout_padding)
                    }

                    textView {
                        id = R.id.home_adapter_tv_time
                    }

                    relativeLayout {
                        view {
                            backgroundColor = Color.BLACK
                        }.lparams(dip(1), matchParent) {
                            centerHorizontally()
                        }

                        imageView {
                            id = R.id.home_adapter_img_point
                        }.lparams(
                                dimen(R.dimen.home_screen_view_margin),
                                dimen(R.dimen.home_screen_view_margin)
                        ) {
                            centerVertically()
                        }

                    }.lparams(wrapContent, matchParent) {
                        leftMargin = dimen(R.dimen.home_screen_view_margin)
                    }

                    imageView(R.drawable.ic_stop).lparams(
                            dimen(R.dimen.home_screen_imgStatus_dimension),
                            dimen(R.dimen.home_screen_imgStatus_dimension)
                    ) {
                        leftMargin = dimen(R.dimen.home_screen_view_margin)
                    }

                    relativeLayout {
                        lparams(matchParent, wrapContent)

                        textView {
                            id = R.id.home_adapter_tv_status
                            textSize = px2dip(dimen(R.dimen.home_screen_tvStatus_size))
                            textColor = Color.BLACK
                            typeface = Typeface.DEFAULT_BOLD
                        }

                        expandableTextView {
                            id = R.id.home_adapter_tv_expandable_description
                            maxLines = 1
                        }.lparams(matchParent, wrapContent) {
                            alignParentLeft()
                            below(R.id.home_adapter_tv_status)
                            rightMargin = dimen(R.dimen.home_screen_expTv_marginRight)
                            topMargin = dimen(R.dimen.home_screen_view_margin)
                        }

                        imageView(R.drawable.ic_keyboard_arrow_down_black_18dp) {
                            visibility = View.GONE
                            id = R.id.home_adapter_img_arrow_down
                        }.lparams {
                            topOf(R.id.home_adapter_tv_expandable_description)
                        }
                    }
                }
            }.view
            itemView.tag = HomeViewHolder(itemView)
            return itemView
        }
    }

    /**
     * Function to custom expandableTextView
     */
    inline fun ViewManager.expandableTextView(init: ExpandableTextView.() -> Unit)
            : ExpandableTextView = ankoView({ ExpandableTextView(it) }, theme = 0, init = init)
}
