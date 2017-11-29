package vn.asiantech.way.ui.home

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
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
import vn.asiantech.way.R
import vn.asiantech.way.data.model.TrackingInformation

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 27/11/2017.
 */
class HomeAdapter(private val context: Context,
                  private val locations: List<TrackingInformation>,
                  val onClickItem: (Int) -> Unit)
    : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    companion object {
        internal const val ID_ITEM_TIME = 3
        internal const val ID_ITEM_STATUS = 3
        internal const val ID_EXPANDABLE_VIEW = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HomeAdapterUI()
            .createView(AnkoContext.create(context, parent, false))
            .tag as? HomeViewHolder

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        holder?.bindHomeViewHolder(locations[position])
    }

    override fun getItemCount(): Int = locations.size

    /**
     * To save data for items in recyclerView of locations
     */
    inner class HomeViewHolder(
            val item: View,
            val tvTime: TextView,
            val tvStatus: TextView,
            val imgArrowDownBlack: ImageView,
            val expTvDescription: ExpandableTextView,
            val imgPoint: ImageView,
            val llItemLocation: LinearLayout
    ) : RecyclerView.ViewHolder(item) {

        internal fun bindHomeViewHolder(location: TrackingInformation) {
            with(location) {
                tvTime.text = time
                tvStatus.text = status
                expTvDescription.text = description
                expTvDescription.post {
                    if (location.isChoose) {
                        if (expTvDescription.lineCount > 1) {
                            imgArrowDownBlack.visibility = View.VISIBLE
                        } else {
                            imgArrowDownBlack.visibility = View.GONE
                        }
                        itemView.setBackgroundResource(R.drawable.bg_item_location_choose)
                        imgPoint.setBackgroundResource(R.drawable.ic_point_white)
                    } else {
                        itemView.setBackgroundResource(R.drawable.bg_item_location_default)
                        imgPoint.setBackgroundResource(R.drawable.ic_point_pink)
                        imgArrowDownBlack.visibility = View.GONE
                    }
                }
            }

            llItemLocation.setOnClickListener {
                onClickItem(adapterPosition)
            }

            imgArrowDownBlack.setOnClickListener {
                expTvDescription.toggle()
            }

            expTvDescription.onExpandListener = object : ExpandableTextView.OnExpandListener {
                override fun onExpand(view: ExpandableTextView) {
                    imgArrowDownBlack.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
                }

                override fun onCollapse(view: ExpandableTextView) {
                    imgArrowDownBlack.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp)
                }
            }
        }
    }

    /**
     * Class to bind ViewHolder with Anko layout
     */
    inner class HomeAdapterUI : AnkoComponent<ViewGroup> {
        internal lateinit var mTvTime: TextView
        internal lateinit var mTvStatus: TextView
        internal lateinit var mImgArrowDownBlack: ImageView
        internal lateinit var mImgPoint: ImageView
        internal lateinit var mExpandableTextView: ExpandableTextView
        internal lateinit var mLLItemLocation: LinearLayout

        override fun createView(ui: AnkoContext<ViewGroup>): View {
            val itemView = ui.apply {
                mLLItemLocation = linearLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    lparams(matchParent, wrapContent) {
                        bottomMargin = dimen(R.dimen.home_screen_linearLayout_margin)
                        topMargin = dimen(R.dimen.home_screen_linearLayout_margin)
                        padding = dimen(R.dimen.home_screen_linearLayout_padding)
                    }

                    mTvTime = textView {
                        id = HomeAdapter.ID_ITEM_TIME
                    }

                    relativeLayout {
                        view {
                            backgroundColor = ContextCompat.getColor(context, R.color.colorBlack)
                        }.lparams(dip(1), matchParent) {
                            centerHorizontally()
                        }

                        mImgPoint = imageView {}.lparams(
                                dimen(R.dimen.home_screen_view_margin),
                                dimen(R.dimen.home_screen_view_margin)
                        ) {
                            centerVertically()
                        }

                    }.lparams(wrapContent, matchParent) {
                        leftMargin = dimen(R.dimen.home_screen_view_margin)
                    }

                    imageView {
                        backgroundResource = R.drawable.ic_stop
                    }.lparams(
                            dimen(R.dimen.home_screen_imgStatus_dimension),
                            dimen(R.dimen.home_screen_imgStatus_dimension)
                    ) {
                        leftMargin = dimen(R.dimen.home_screen_view_margin)
                    }

                    relativeLayout {
                        lparams(matchParent, wrapContent)

                        mTvStatus = textView {
                            id = HomeAdapter.ID_ITEM_STATUS
                            textSize = px2dip(dimen(R.dimen.home_screen_tvStatus_size))
                            textColor = ContextCompat.getColor(context, R.color.colorBlack)
                            typeface = Typeface.DEFAULT_BOLD
                        }

                        mExpandableTextView = expandableTextView {
                            id = HomeAdapter.ID_EXPANDABLE_VIEW
                            maxLines = 1
                        }.lparams(matchParent, wrapContent) {
                            alignParentLeft()
                            below(HomeAdapter.ID_ITEM_STATUS)
                            rightMargin = dimen(R.dimen.home_screen_expTv_marginRight)
                            topMargin = dimen(R.dimen.home_screen_view_margin)
                        }

                        mImgArrowDownBlack = imageView {
                            visibility = View.GONE
                            backgroundResource = R.drawable.ic_keyboard_arrow_down_black_18dp
                        }.lparams {
                            topOf(HomeAdapter.ID_EXPANDABLE_VIEW)
                        }
                    }
                }
            }.view
            itemView.tag = HomeViewHolder(
                    itemView,
                    mTvTime,
                    mTvStatus,
                    mImgArrowDownBlack,
                    mExpandableTextView,
                    mImgPoint,
                    mLLItemLocation
            )
            return itemView
        }
    }

}

/**
 * Function to custom expandableTextView
 */
inline fun ViewManager.expandableTextView(init: ExpandableTextView.() -> Unit)
        : ExpandableTextView {
    return ankoView({ ExpandableTextView(it) }, theme = 0, init = init)
}
