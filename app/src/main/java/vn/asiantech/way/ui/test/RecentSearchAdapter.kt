package vn.asiantech.way.ui.test

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.textResource
import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 19/12/2017
 */
class RecentSearchAdapter(val recents: MutableList<RecentModel>, val popular: MutableList<PopularModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder?
            = HeaderSearchItemUI().createView(AnkoContext.Companion.create(parent.context,
            parent, false)).tag as? HeaderViewHolder

    override
    fun getItemCount(): Int {
        var size = recents.size + popular.size
        if (recents.size > 0) {
            size++
        }
        if (popular.size > 0) {
            size++
        }
        return size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.onBind(R.string.coming_soon)
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHeader: TextView = itemView.find(R.id.text_search_recent_iten_header_name)

        fun onBind(@StringRes stringId: Int) {
            tvHeader.textResource = stringId
        }
    }
}
