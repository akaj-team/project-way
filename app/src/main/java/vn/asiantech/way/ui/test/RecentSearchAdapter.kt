package vn.asiantech.way.ui.test

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.textResource
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class RecentSearchAdapter(private val items: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_RECENT = 1
        private const val TYPE_POPULAR = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            TYPE_RECENT -> RecentSearchItemUI().createView(AnkoContext.Companion.create(parent.context, parent, false))
                    .tag as? RecentSearchItemViewHolder

            TYPE_POPULAR -> PopularSearchItemUI().createView(AnkoContext.Companion.create(parent.context, parent, false))
                    .tag as? PopularSearchItemViewHolder

            else -> HeaderSearchItemUI().createView(AnkoContext.Companion.create(parent.context, parent, false))
                    .tag as? HeaderViewHolder
        }
    }
    override
    fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecentSearchItemViewHolder -> holder.onBind(items[position])

            is PopularSearchItemViewHolder -> holder.onBind(items[position])

            is HeaderViewHolder -> holder.onBind(items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RecentModel -> TYPE_RECENT

            is PopularModel -> TYPE_POPULAR

            else -> TYPE_HEADER
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHeader: TextView = itemView.find(R.id.text_search_recent_iten_header_name)

        fun onBind(item: Any) {
            val header = item as? HeaderModel
            if (header != null) {
                tvHeader.textResource = header.name
            }
        }
    }

    class RecentSearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgIcon: RoundedImageView = itemView.find(R.id.text_search_recent_item_img_icon)
        private val tvName: TextView = itemView.findViewById(R.id.text_search_recent_item_tv_name)
        private val tvDescription: TextView = itemView.find(R.id.text_search_recent_item_tv_description)

        fun onBind(item: Any) {
            val recent = item as? RecentModel
            if (recent != null) {
                Glide.with(itemView.context).load(item.icon).into(imgIcon)
                tvName.text = recent.name
                tvDescription.text = recent.description
            }
        }
    }

    class PopularSearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgIcon: RoundedImageView = itemView.find(R.id.text_search_popular_item_img_icon)
        private val tvName: TextView = itemView.findViewById(R.id.text_search_popular_item_tv_name)

        fun onBind(item: Any) {
            val popular = item as? PopularModel
            if (popular != null) {
                Glide.with(itemView.context).load(item.icon).into(imgIcon)
                tvName.text = popular.name
            }
        }
    }
}
