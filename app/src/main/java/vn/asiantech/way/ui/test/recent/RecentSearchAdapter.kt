package vn.asiantech.way.ui.test.recent

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textResource
import vn.asiantech.way.R
import vn.asiantech.way.ui.test.tag.TagSearchItemUI

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class RecentSearchAdapter(private val items: MutableList<Any>, internal var onItemClicked: (Any) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_RECENT = 1
        private const val TYPE_POPULAR = 2
        private const val TYPE_TAG = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            TYPE_RECENT -> {
                val recentSearchItemUI = RecentSearchItemUI()
                RecentSearchItemViewHolder(recentSearchItemUI,
                        recentSearchItemUI.createView(AnkoContext.Companion.create(parent.context, parent, false)))
            }

            TYPE_POPULAR -> {
                val popularSearchItemUI = PopularSearchItemUI()
                PopularSearchItemViewHolder(popularSearchItemUI,
                        popularSearchItemUI.createView(AnkoContext.Companion.create(parent.context, parent, false)))
            }

            TYPE_TAG -> {
                val tagSearchItemUI = TagSearchItemUI()
                TagSearchItemViewHolder(tagSearchItemUI,
                        tagSearchItemUI.createView(AnkoContext.Companion.create(parent.context, parent, false)))
            }

            else -> {
                val headerSearchItemUI = HeaderSearchItemUI()
                HeaderViewHolder(headerSearchItemUI,
                        headerSearchItemUI.createView(AnkoContext.Companion.create(parent.context, parent, false)))
            }
        }
    }

    override
    fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecentSearchItemViewHolder -> holder.onBind(items[position])

            is PopularSearchItemViewHolder -> holder.onBind(items[position])

            is TagSearchItemViewHolder -> holder.onBind(items[position])

            is HeaderViewHolder -> holder.onBind(items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RecentModel -> TYPE_RECENT

            is PopularModel -> TYPE_POPULAR

            is TagModel -> TYPE_TAG

            else -> TYPE_HEADER
        }
    }

    inner class HeaderViewHolder(val ui: HeaderSearchItemUI, itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        fun onBind(item: Any) {
            val header = item as? HeaderModel
            if (header != null) {
                ui.tvHeaderName.textResource = header.name
            }
        }
    }

    inner class RecentSearchItemViewHolder(val ui: RecentSearchItemUI, itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClick {
                onItemClicked(items[adapterPosition])
            }
        }

        fun onBind(item: Any) {
            val recent = item as? RecentModel
            if (recent != null) {
                Glide.with(itemView.context).load(item.icon).into(ui.imgRecentIcon)
                ui.tvRecentName.text = recent.name
                ui.tvRecentDescription.text = recent.description
            }
        }
    }

    inner class PopularSearchItemViewHolder(val ui: PopularSearchItemUI, itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClick {
                onItemClicked(items[adapterPosition])
            }
        }

        fun onBind(item: Any) {
            val popular = item as? PopularModel
            if (popular != null) {
                Glide.with(itemView.context).load(item.icon).into(ui.imgPopularIcon)
                ui.tvPopularName.text = popular.name
            }
        }
    }

    inner class TagSearchItemViewHolder(val ui: TagSearchItemUI, itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.onClick {
                onItemClicked(items[adapterPosition])
            }
        }

        fun onBind(item: Any) {
            val tag = item as? TagModel
            if (tag != null) {
                ui.tvTagName.text = tag.name
                ui.tvPostCount.text = itemView.context.getString(R.string.text_search_tag_item_post_count, item.postCount)
            }
        }
    }
}
